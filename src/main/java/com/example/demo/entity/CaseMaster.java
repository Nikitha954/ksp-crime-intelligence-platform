package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_master")
public class CaseMaster {

    @Id
    private Long caseMasterID;

    private String crimeNo;
    private String caseNo;

    private LocalDate crimeRegisteredDate;

    private Integer policeStationID;
    private String policeStationName;

    private Integer districtID;
    private String districtName;

    private Integer caseCategoryID;
    private String caseCategoryName;

    private Integer gravityOffenceID;
    private String gravityOffenceName;

    private Integer crimeMajorHeadID;
    private Integer crimeMinorHeadID;
    private String crimeSubHeadName;

    private Integer caseStatusID;
    private String caseStatusName;

    private LocalDateTime incidentFromDate;
    private LocalDateTime incidentToDate;
    private LocalDateTime infoReceivedPSDate;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String briefFacts;

    public CaseMaster() {}

    public Long getCaseMasterID() { return caseMasterID; }
    public void setCaseMasterID(Long caseMasterID) { this.caseMasterID = caseMasterID; }

    public String getCrimeNo() { return crimeNo; }
    public void setCrimeNo(String crimeNo) { this.crimeNo = crimeNo; }

    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }

    public LocalDate getCrimeRegisteredDate() { return crimeRegisteredDate; }
    public void setCrimeRegisteredDate(LocalDate crimeRegisteredDate) { this.crimeRegisteredDate = crimeRegisteredDate; }

    public Integer getPoliceStationID() { return policeStationID; }
    public void setPoliceStationID(Integer policeStationID) { this.policeStationID = policeStationID; }

    public String getPoliceStationName() { return policeStationName; }
    public void setPoliceStationName(String policeStationName) { this.policeStationName = policeStationName; }

    public Integer getDistrictID() { return districtID; }
    public void setDistrictID(Integer districtID) { this.districtID = districtID; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public Integer getCaseCategoryID() { return caseCategoryID; }
    public void setCaseCategoryID(Integer caseCategoryID) { this.caseCategoryID = caseCategoryID; }

    public String getCaseCategoryName() { return caseCategoryName; }
    public void setCaseCategoryName(String caseCategoryName) { this.caseCategoryName = caseCategoryName; }

    public Integer getGravityOffenceID() { return gravityOffenceID; }
    public void setGravityOffenceID(Integer gravityOffenceID) { this.gravityOffenceID = gravityOffenceID; }

    public String getGravityOffenceName() { return gravityOffenceName; }
    public void setGravityOffenceName(String gravityOffenceName) { this.gravityOffenceName = gravityOffenceName; }

    public Integer getCrimeMajorHeadID() { return crimeMajorHeadID; }
    public void setCrimeMajorHeadID(Integer crimeMajorHeadID) { this.crimeMajorHeadID = crimeMajorHeadID; }

    public Integer getCrimeMinorHeadID() { return crimeMinorHeadID; }
    public void setCrimeMinorHeadID(Integer crimeMinorHeadID) { this.crimeMinorHeadID = crimeMinorHeadID; }

    public String getCrimeSubHeadName() { return crimeSubHeadName; }
    public void setCrimeSubHeadName(String crimeSubHeadName) { this.crimeSubHeadName = crimeSubHeadName; }

    public Integer getCaseStatusID() { return caseStatusID; }
    public void setCaseStatusID(Integer caseStatusID) { this.caseStatusID = caseStatusID; }

    public String getCaseStatusName() { return caseStatusName; }
    public void setCaseStatusName(String caseStatusName) { this.caseStatusName = caseStatusName; }

    public LocalDateTime getIncidentFromDate() { return incidentFromDate; }
    public void setIncidentFromDate(LocalDateTime incidentFromDate) { this.incidentFromDate = incidentFromDate; }

    public LocalDateTime getIncidentToDate() { return incidentToDate; }
    public void setIncidentToDate(LocalDateTime incidentToDate) { this.incidentToDate = incidentToDate; }

    public LocalDateTime getInfoReceivedPSDate() { return infoReceivedPSDate; }
    public void setInfoReceivedPSDate(LocalDateTime infoReceivedPSDate) { this.infoReceivedPSDate = infoReceivedPSDate; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getBriefFacts() { return briefFacts; }
    public void setBriefFacts(String briefFacts) { this.briefFacts = briefFacts; }
}
