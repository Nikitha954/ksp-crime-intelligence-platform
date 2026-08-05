package com.example.demo.controller;

import com.example.demo.entity.Accused;
import com.example.demo.entity.CaseMaster;
import com.example.demo.repository.AccusedRepository;
import com.example.demo.repository.CaseMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired private CaseMasterRepository caseRepo;
    @Autowired private AccusedRepository accusedRepo;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCases(
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String crimeSubHead,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String caseStatus) {

        Specification<CaseMaster> spec = Specification.where(null);

        if (district != null && !district.trim().isEmpty() && !district.equalsIgnoreCase("All")) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("districtName")), "%" + district.toLowerCase() + "%"));
        }

        if (crimeSubHead != null && !crimeSubHead.trim().isEmpty() && !crimeSubHead.equalsIgnoreCase("All")) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("crimeSubHeadName")), "%" + crimeSubHead.toLowerCase() + "%"));
        }

        if (caseStatus != null && !caseStatus.trim().isEmpty() && !caseStatus.equalsIgnoreCase("All")) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("caseStatusName")), caseStatus.toLowerCase()));
        }

        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            LocalDate dFrom = LocalDate.parse(dateFrom);
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("crimeRegisteredDate"), dFrom));
        }

        if (dateTo != null && !dateTo.trim().isEmpty()) {
            LocalDate dTo = LocalDate.parse(dateTo);
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("crimeRegisteredDate"), dTo));
        }

        List<CaseMaster> results = caseRepo.findAll(spec);

        Map<String, Object> response = new HashMap<>();
        response.put("count", results.size());
        response.put("cases", results);
        response.put("dateFrom", dateFrom);
        response.put("dateTo", dateTo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
public ResponseEntity<Map<String, Object>> getSummary() {
    Map<String, Object> summary = new HashMap<>();

    // 1. Total FIR Count
    long totalCases = caseRepo.count();
    summary.put("totalCases", totalCases);

    // 2. Breakdown by Crime SubHead
    List<Object[]> subHeadCounts = caseRepo.countByCrimeSubHead();
    List<Map<String, Object>> byCrimeType = new ArrayList<>();
    if (subHeadCounts != null) {
        for (Object[] row : subHeadCounts) {
            if (row != null && row.length >= 2) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", row[0] != null ? row[0].toString() : "Unspecified");
                item.put("value", row[1] != null ? row[1] : 0);
                byCrimeType.add(item);
            }
        }
    }
    summary.put("byCrimeType", byCrimeType);

    // 3. Breakdown by District
    List<Object[]> districtCounts = caseRepo.countByDistrict();
    List<Map<String, Object>> byDistrict = new ArrayList<>();
    if (districtCounts != null) {
        for (Object[] row : districtCounts) {
            if (row != null && row.length >= 2) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", row[0] != null ? row[0].toString() : "Unspecified");
                item.put("value", row[1] != null ? row[1] : 0);
                byDistrict.add(item);
            }
        }
    }
    summary.put("byDistrict", byDistrict);

    // 4. Monthly Trend (Null-Safe & ClassCast-Safe)
    List<Object[]> monthCounts = caseRepo.countByMonth();
    List<Map<String, Object>> monthlyTrend = new ArrayList<>();
    if (monthCounts != null) {
        for (Object[] row : monthCounts) {
            if (row != null && row.length >= 3 && row[0] != null && row[1] != null) {
                try {
                    int year = Integer.parseInt(row[0].toString().replaceAll("\\D", ""));
                    int month = Integer.parseInt(row[1].toString().replaceAll("\\D", ""));
                    
                    Map<String, Object> item = new HashMap<>();
                    item.put("month", String.format("%d-%02d", year, month));
                    item.put("count", row[2] != null ? row[2] : 0);
                    monthlyTrend.add(item);
                } catch (Exception ignored) {
                    // Skip malformed row gracefully without crashing the endpoint
                }
            }
        }
    }
    summary.put("monthlyTrend", monthlyTrend);

    return ResponseEntity.ok(summary);
}
    @GetMapping("/hotspots")
    public ResponseEntity<List<Map<String, Object>>> getHotspots() {
        LocalDate now = LocalDate.now();
        LocalDate recentDate = now.minusDays(30);
        LocalDate rollingDate = now.minusDays(180);

        List<Object[]> rows = caseRepo.getHotspotAggregates(recentDate, rollingDate);
        List<Map<String, Object>> hotspots = new ArrayList<>();

        for (Object[] r : rows) {
            Map<String, Object> spot = new HashMap<>();
            spot.put("districtID", r[0]);
            spot.put("districtName", r[1]);
            spot.put("policeStationID", r[2]);
            spot.put("policeStationName", r[3]);
            spot.put("latitude", r[4]);
            spot.put("longitude", r[5]);

            long totalCount = ((Number) r[6]).longValue();
            long recentCount = ((Number) r[7]).longValue();
            long rollingCount = ((Number) r[8]).longValue();

            spot.put("totalCount", totalCount);
            spot.put("recent30DayCount", recentCount);
            spot.put("rolling6MonthCount", rollingCount);

            double sixMonthMonthlyAvg = rollingCount / 6.0;
            // Flag as emerging if recent 30-day count > 1.2x 6-month monthly average
            boolean isEmerging = recentCount > 3 && (sixMonthMonthlyAvg == 0 || recentCount > (1.2 * sixMonthMonthlyAvg));
            spot.put("emerging", isEmerging);

            hotspots.add(spot);
        }

        return ResponseEntity.ok(hotspots);
    }
}
