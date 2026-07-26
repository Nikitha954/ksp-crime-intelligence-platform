package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "case_status_master")
public class CaseStatusMaster {

    @Id
    private Integer caseStatusID;

    private String caseStatusName;

    public CaseStatusMaster() {}

    public CaseStatusMaster(Integer caseStatusID, String caseStatusName) {
        this.caseStatusID = caseStatusID;
        this.caseStatusName = caseStatusName;
    }

    public Integer getCaseStatusID() { return caseStatusID; }
    public void setCaseStatusID(Integer caseStatusID) { this.caseStatusID = caseStatusID; }

    public String getCaseStatusName() { return caseStatusName; }
    public void setCaseStatusName(String caseStatusName) { this.caseStatusName = caseStatusName; }
}
