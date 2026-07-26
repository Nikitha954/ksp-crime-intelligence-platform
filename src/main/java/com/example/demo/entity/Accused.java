package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accused")
public class Accused {

    @Id
    private Long accusedMasterID;

    private Long caseMasterID;
    private String accusedName;
    private Integer ageYear;
    private String genderID;
    private String personID;

    public Accused() {}

    public Long getAccusedMasterID() { return accusedMasterID; }
    public void setAccusedMasterID(Long accusedMasterID) { this.accusedMasterID = accusedMasterID; }

    public Long getCaseMasterID() { return caseMasterID; }
    public void setCaseMasterID(Long caseMasterID) { this.caseMasterID = caseMasterID; }

    public String getAccusedName() { return accusedName; }
    public void setAccusedName(String accusedName) { this.accusedName = accusedName; }

    public Integer getAgeYear() { return ageYear; }
    public void setAgeYear(Integer ageYear) { this.ageYear = ageYear; }

    public String getGenderID() { return genderID; }
    public void setGenderID(String genderID) { this.genderID = genderID; }

    public String getPersonID() { return personID; }
    public void setPersonID(String personID) { this.personID = personID; }
}
