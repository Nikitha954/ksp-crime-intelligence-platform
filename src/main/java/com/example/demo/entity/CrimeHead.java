package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crime_head")
public class CrimeHead {

    @Id
    private Integer crimeHeadID;

    private String crimeGroupName;

    public CrimeHead() {}

    public CrimeHead(Integer crimeHeadID, String crimeGroupName) {
        this.crimeHeadID = crimeHeadID;
        this.crimeGroupName = crimeGroupName;
    }

    public Integer getCrimeHeadID() { return crimeHeadID; }
    public void setCrimeHeadID(Integer crimeHeadID) { this.crimeHeadID = crimeHeadID; }

    public String getCrimeGroupName() { return crimeGroupName; }
    public void setCrimeGroupName(String crimeGroupName) { this.crimeGroupName = crimeGroupName; }
}
