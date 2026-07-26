package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired private CaseMasterRepository caseRepo;
    @Autowired private VictimRepository victimRepo;
    @Autowired private AccusedRepository accusedRepo;
    @Autowired private ComplainantDetailsRepository complainantRepo;
    @Autowired private ArrestSurrenderRepository arrestRepo;

    @Autowired private DistrictRepository districtRepo;
    @Autowired private UnitRepository unitRepo;
    @Autowired private CrimeSubHeadRepository subHeadRepo;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) throws Exception {
        if (caseRepo.count() > 0) {
            System.out.println("KSP Database already contains " + caseRepo.count() + " cases. Skipping auto-seeding.");
            return;
        }

        ClassPathResource resource = new ClassPathResource("data/ksp_crime_data.json");
        if (!resource.exists()) {
            System.err.println("data/ksp_crime_data.json not found on classpath. Ensure it's in src/main/resources/data/");
            return;
        }

        System.out.println("Auto-seeding KSP Crime Analytics database from classpath resource...");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try (InputStream is = resource.getInputStream()) {
            root = mapper.readTree(is);
        }

        // 1. Seed Districts
        if (root.has("districts")) {
            List<District> dists = new ArrayList<>();
            for (JsonNode node : root.get("districts")) {
                dists.add(new District(
                        node.get("DistrictID").asInt(),
                        node.get("DistrictName").asText(),
                        node.get("StateID").asInt()
                ));
            }
            districtRepo.saveAll(dists);
        }

        // 2. Seed CrimeSubHeads
        if (root.has("crimeSubHeads")) {
            List<CrimeSubHead> subHeads = new ArrayList<>();
            for (JsonNode node : root.get("crimeSubHeads")) {
                subHeads.add(new CrimeSubHead(
                        node.get("CrimeSubHeadID").asInt(),
                        node.get("CrimeHeadID").asInt(),
                        node.get("CrimeHeadName").asText()
                ));
            }
            subHeadRepo.saveAll(subHeads);
        }

        // 3. Seed Cases
        List<CaseMaster> cases = new ArrayList<>();
        if (root.has("cases")) {
            for (JsonNode c : root.get("cases")) {
                CaseMaster cm = new CaseMaster();
                cm.setCaseMasterID(c.get("CaseMasterID").asLong());
                cm.setCrimeNo(c.get("CrimeNo").asText());
                cm.setCaseNo(c.get("CaseNo").asText());
                cm.setCrimeRegisteredDate(LocalDate.parse(c.get("CrimeRegisteredDate").asText(), DATE_FMT));
                cm.setPoliceStationID(c.get("PoliceStationID").asInt());
                cm.setPoliceStationName(c.get("PoliceStationName").asText());
                cm.setDistrictID(c.get("DistrictID").asInt());
                cm.setDistrictName(c.get("DistrictName").asText());
                cm.setCaseCategoryID(c.get("CaseCategoryID").asInt());
                cm.setCaseCategoryName(c.get("CaseCategoryName").asText());
                cm.setGravityOffenceID(c.get("GravityOffenceID").asInt());
                cm.setGravityOffenceName(c.get("GravityOffenceName").asText());
                cm.setCrimeMajorHeadID(c.get("CrimeMajorHeadID").asInt());
                cm.setCrimeMinorHeadID(c.get("CrimeMinorHeadID").asInt());
                cm.setCrimeSubHeadName(c.get("CrimeSubHeadName").asText());
                cm.setCaseStatusID(c.get("CaseStatusID").asInt());
                cm.setCaseStatusName(c.get("CaseStatusName").asText());
                cm.setIncidentFromDate(LocalDateTime.parse(c.get("IncidentFromDate").asText(), DATETIME_FMT));
                cm.setIncidentToDate(LocalDateTime.parse(c.get("IncidentToDate").asText(), DATETIME_FMT));
                cm.setInfoReceivedPSDate(LocalDateTime.parse(c.get("InfoReceivedPSDate").asText(), DATETIME_FMT));
                cm.setLatitude(c.get("latitude").asDouble());
                cm.setLongitude(c.get("longitude").asDouble());
                cm.setBriefFacts(c.get("BriefFacts").asText());
                cases.add(cm);
            }
            caseRepo.saveAll(cases);
        }

        // 4. Seed Victims
        if (root.has("victims")) {
            List<Victim> victims = new ArrayList<>();
            for (JsonNode v : root.get("victims")) {
                Victim vic = new Victim();
                vic.setVictimMasterID(v.get("VictimMasterID").asLong());
                vic.setCaseMasterID(v.get("CaseMasterID").asLong());
                vic.setVictimName(v.get("VictimName").asText());
                vic.setAgeYear(v.get("AgeYear").asInt());
                vic.setGenderID(v.get("GenderID").asText());
                vic.setVictimPolice(v.get("VictimPolice").asBoolean());
                victims.add(vic);
            }
            victimRepo.saveAll(victims);
        }

        // 5. Seed Complainants
        if (root.has("complainants")) {
            List<ComplainantDetails> complainants = new ArrayList<>();
            for (JsonNode comp : root.get("complainants")) {
                ComplainantDetails cd = new ComplainantDetails();
                cd.setComplainantID(comp.get("ComplainantID").asLong());
                cd.setCaseMasterID(comp.get("CaseMasterID").asLong());
                cd.setComplainantName(comp.get("ComplainantName").asText());
                cd.setAgeYear(comp.get("AgeYear").asInt());
                cd.setGenderID(comp.get("GenderID").asText());
                cd.setOccupationID(comp.get("OccupationID").asText());
                cd.setReligionID(comp.get("ReligionID").asText());
                cd.setCasteID(comp.get("CasteID").asText());
                complainants.add(cd);
            }
            complainantRepo.saveAll(complainants);
        }

        // 6. Seed Accused
        if (root.has("accused")) {
            List<Accused> accusedList = new ArrayList<>();
            for (JsonNode a : root.get("accused")) {
                Accused acc = new Accused();
                acc.setAccusedMasterID(a.get("AccusedMasterID").asLong());
                acc.setCaseMasterID(a.get("CaseMasterID").asLong());
                acc.setAccusedName(a.get("AccusedName").asText());
                acc.setAgeYear(a.get("AgeYear").asInt());
                acc.setGenderID(a.get("GenderID").asText());
                acc.setPersonID(a.get("PersonID").asText());
                accusedList.add(acc);
            }
            accusedRepo.saveAll(accusedList);
        }

        // 7. Seed Arrests
        if (root.has("arrests")) {
            List<ArrestSurrender> arrests = new ArrayList<>();
            for (JsonNode ar : root.get("arrests")) {
                ArrestSurrender arr = new ArrestSurrender();
                arr.setArrestSurrenderID(ar.get("ArrestSurrenderID").asLong());
                arr.setCaseMasterID(ar.get("CaseMasterID").asLong());
                arr.setAccusedMasterID(ar.get("AccusedMasterID").asLong());
                arr.setArrestSurrenderTypeID(ar.get("ArrestSurrenderTypeID").asInt());
                arr.setArrestSurrenderDate(LocalDate.parse(ar.get("ArrestSurrenderDate").asText(), DATE_FMT));
                arr.setPoliceStationID(ar.get("PoliceStationID").asInt());
                arr.setIoid(ar.get("IOID").asText());
                arrests.add(arr);
            }
            arrestRepo.saveAll(arrests);
        }

        System.out.println("SUCCESS: Database auto-seeded with " + cases.size() + " KSP FIR records.");
    }
}
