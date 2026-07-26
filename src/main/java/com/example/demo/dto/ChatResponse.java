package com.example.demo.dto;

import com.example.demo.entity.CaseMaster;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatResponse {
    private UUID sessionId;
    private String response;
    private Map<String, Object> parsedFilters;
    private Integer recordsCount;
    private List<CaseMaster> records;
    private String explainabilityCaption;

    public ChatResponse() {}

    public ChatResponse(UUID sessionId, String response) {
        this.sessionId = sessionId;
        this.response = response;
    }

    public ChatResponse(UUID sessionId, String response, Map<String, Object> parsedFilters, Integer recordsCount, List<CaseMaster> records, String explainabilityCaption) {
        this.sessionId = sessionId;
        this.response = response;
        this.parsedFilters = parsedFilters;
        this.recordsCount = recordsCount;
        this.records = records;
        this.explainabilityCaption = explainabilityCaption;
    }

    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public Map<String, Object> getParsedFilters() { return parsedFilters; }
    public void setParsedFilters(Map<String, Object> parsedFilters) { this.parsedFilters = parsedFilters; }

    public Integer getRecordsCount() { return recordsCount; }
    public void setRecordsCount(Integer recordsCount) { this.recordsCount = recordsCount; }

    public List<CaseMaster> getRecords() { return records; }
    public void setRecords(List<CaseMaster> records) { this.records = records; }

    public String getExplainabilityCaption() { return explainabilityCaption; }
    public void setExplainabilityCaption(String explainabilityCaption) { this.explainabilityCaption = explainabilityCaption; }
}