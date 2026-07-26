package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "complainant_details")
public class ComplainantDetails {

    @Id
    private Long complainantID;

    private Long caseMasterID;
    private String complainantName;
    private Integer ageYear;
    private String genderID;
    private String occupationID;
    private String religionID;
    private String casteID;

    public ComplainantDetails() {}

    public Long getComplainantID() { return complainantID; }
    public void setComplainantID(Long complainantID) { this.complainantID = complainantID; }

    public Long getCaseMasterID() { return caseMasterID; }
    public void setCaseMasterID(Long caseMasterID) { this.caseMasterID = caseMasterID; }

    public String getComplainantName() { return complainantName; }
    public void setComplainantName(String complainantName) { this.complainantName = complainantName; }

    public Integer getAgeYear() { return ageYear; }
    public void setAgeYear(Integer ageYear) { this.ageYear = ageYear; }

    public String getGenderID() { return genderID; }
    public void setGenderID(String genderID) { this.genderID = genderID; }

    public String getOccupationID() { return occupationID; }
    public void setOccupationID(String occupationID) { this.occupationID = occupationID; }

    public String getReligionID() { return religionID; }
    public void setReligionID(String religionID) { this.religionID = religionID; }

    public String getCasteID() { return casteID; }
    public void setCasteID(String casteID) { this.casteID = casteID; }
}
