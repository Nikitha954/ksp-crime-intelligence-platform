package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crime_sub_head")
public class CrimeSubHead {

    @Id
    private Integer crimeSubHeadID;

    private Integer crimeHeadID;
    private String crimeHeadName; // Note: despite the schema field name, this represents the SubHead label

    public CrimeSubHead() {}

    public CrimeSubHead(Integer crimeSubHeadID, Integer crimeHeadID, String crimeHeadName) {
        this.crimeSubHeadID = crimeSubHeadID;
        this.crimeHeadID = crimeHeadID;
        this.crimeHeadName = crimeHeadName;
    }

    public Integer getCrimeSubHeadID() { return crimeSubHeadID; }
    public void setCrimeSubHeadID(Integer crimeSubHeadID) { this.crimeSubHeadID = crimeSubHeadID; }

    public Integer getCrimeHeadID() { return crimeHeadID; }
    public void setCrimeHeadID(Integer crimeHeadID) { this.crimeHeadID = crimeHeadID; }

    public String getCrimeHeadName() { return crimeHeadName; }
    public void setCrimeHeadName(String crimeHeadName) { this.crimeHeadName = crimeHeadName; }
}
