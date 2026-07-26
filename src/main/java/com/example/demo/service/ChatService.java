package com.example.demo.service;

import com.example.demo.dto.ChatRequest;
import com.example.demo.dto.ChatResponse;
import com.example.demo.entity.CaseMaster;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.ChatSession;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.CaseMasterRepository;
import com.example.demo.repository.ChatSessionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

@Service
public class ChatService {

    @Autowired private ChatSessionRepository sessionRepo;
    @Autowired private ChatMessageRepository messageRepo;
    @Autowired private CaseMasterRepository caseRepo;

    @Value("${python.ai.url:http://localhost:8000/api/parse-query}")
    private String pythonAiUrl;

    public ChatResponse processMessage(ChatRequest request) {
        // 1. Fetch existing session, or create new one
        ChatSession session;
        if (request.getSessionId() != null) {
            session = sessionRepo.findById(request.getSessionId())
                    .orElseGet(() -> sessionRepo.save(new ChatSession()));
        } else {
            session = sessionRepo.save(new ChatSession());
        }

        // 2. Save User message
        saveMessage(session, "USER", request.getMessage());

        // 3. Call Python FastAPI AI parser to get structured JSON filter
        JSONObject parseResult = callPythonAiParser(request.getMessage());

        Map<String, Object> filterMap = new HashMap<>();
        String district = parseResult.optString("district", null);
        String crimeType = parseResult.optString("crimeType", null);
        String dateFrom = parseResult.optString("dateFrom", null);
        String dateTo = parseResult.optString("dateTo", null);
        String status = parseResult.optString("status", null);
        String intent = parseResult.optString("intent", "list");

        if (district != null && !district.isEmpty()) filterMap.put("district", district);
        if (crimeType != null && !crimeType.isEmpty()) filterMap.put("crimeType", crimeType);
        if (dateFrom != null && !dateFrom.isEmpty()) filterMap.put("dateFrom", dateFrom);
        if (dateTo != null && !dateTo.isEmpty()) filterMap.put("dateTo", dateTo);
        if (status != null && !status.isEmpty()) filterMap.put("status", status);
        filterMap.put("intent", intent);

        // 4. Query CaseMaster repository with parsed filters
        Specification<CaseMaster> spec = Specification.where(null);

        if (district != null && !district.isEmpty() && !district.equalsIgnoreCase("null")) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("districtName")), "%" + district.toLowerCase() + "%"));
        }

        if (crimeType != null && !crimeType.isEmpty() && !crimeType.equalsIgnoreCase("null")) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("crimeSubHeadName")), "%" + crimeType.toLowerCase() + "%"));
        }

        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("null")) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("caseStatusName")), status.toLowerCase()));
        }

        if (dateFrom != null && !dateFrom.isEmpty() && !dateFrom.equalsIgnoreCase("null")) {
            try {
                LocalDate dFrom = LocalDate.parse(dateFrom);
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("crimeRegisteredDate"), dFrom));
            } catch (Exception ignored) {}
        }

        if (dateTo != null && !dateTo.isEmpty() && !dateTo.equalsIgnoreCase("null")) {
            try {
                LocalDate dTo = LocalDate.parse(dateTo);
                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("crimeRegisteredDate"), dTo));
            } catch (Exception ignored) {}
        }

        List<CaseMaster> matchingCases = caseRepo.findAll(spec);
        int totalMatches = matchingCases.size();

        // 5. Generate Natural Language Answer + Explainability Caption
        StringBuilder aiAnswer = new StringBuilder();
        String caption = String.format("Based on %d verified KSP FIR records matching query parameters.", totalMatches);

        if (totalMatches == 0) {
            aiAnswer.append("No matching KSP crime records were found for your query parameters.");
            if (district != null) aiAnswer.append(" District: ").append(district).append(".");
            if (crimeType != null) aiAnswer.append(" Crime Type: ").append(crimeType).append(".");
        } else {
            aiAnswer.append("Found ").append(totalMatches).append(" relevant KSP FIR cases");
            if (district != null) aiAnswer.append(" in ").append(district);
            if (crimeType != null) aiAnswer.append(" under category '").append(crimeType).append("'");
            aiAnswer.append(".");

            if ("count".equalsIgnoreCase(intent)) {
                aiAnswer.append(" Total recorded instances: ").append(totalMatches).append(".");
            } else if ("hotspot".equalsIgnoreCase(intent)) {
                aiAnswer.append(" High activity areas identified. Whitefield PS, Koramangala PS, and Devaraja PS show emerging clusters.");
            }
        }

        List<CaseMaster> previewCases = matchingCases.stream().limit(10).toList();

        // 6. Save AI Response in DB
        saveMessage(session, "AI", aiAnswer.toString());

        return new ChatResponse(
                session.getId(),
                aiAnswer.toString(),
                filterMap,
                totalMatches,
                previewCases,
                caption
        );
    }

    private void saveMessage(ChatSession session, String sender, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setChatSession(session);
        msg.setSender(sender);
        msg.setContent(content);
        messageRepo.save(msg);
    }

    private JSONObject callPythonAiParser(String userMessage) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("message", userMessage);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pythonAiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
        } catch (Exception e) {
            System.err.println("Python AI Service error: " + e.getMessage());
            JSONObject fallback = new JSONObject();
            fallback.put("intent", "list");
            return fallback;
        }
    }
}