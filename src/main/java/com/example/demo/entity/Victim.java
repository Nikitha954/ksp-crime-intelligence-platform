package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "victim")
public class Victim {

    @Id
    private Long victimMasterID;

    private Long caseMasterID;
    private String victimName;
    private Integer ageYear;
    private String genderID;
    private Boolean victimPolice;

    public Victim() {}

    public Long getVictimMasterID() { return victimMasterID; }
    public void setVictimMasterID(Long victimMasterID) { this.victimMasterID = victimMasterID; }

    public Long getCaseMasterID() { return caseMasterID; }
    public void setCaseMasterID(Long caseMasterID) { this.caseMasterID = caseMasterID; }

    public String getVictimName() { return victimName; }
    public void setVictimName(String victimName) { this.victimName = victimName; }

    public Integer getAgeYear() { return ageYear; }
    public void setAgeYear(Integer ageYear) { this.ageYear = ageYear; }

    public String getGenderID() { return genderID; }
    public void setGenderID(String genderID) { this.genderID = genderID; }

    public Boolean getVictimPolice() { return victimPolice; }
    public void setVictimPolice(Boolean victimPolice) { this.victimPolice = victimPolice; }
}
