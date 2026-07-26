package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gravity_offence")
public class GravityOffence {

    @Id
    private Integer gravityOffenceID;

    private String lookupValue;

    public GravityOffence() {}

    public GravityOffence(Integer gravityOffenceID, String lookupValue) {
        this.gravityOffenceID = gravityOffenceID;
        this.lookupValue = lookupValue;
    }

    public Integer getGravityOffenceID() { return gravityOffenceID; }
    public void setGravityOffenceID(Integer gravityOffenceID) { this.gravityOffenceID = gravityOffenceID; }

    public String getLookupValue() { return lookupValue; }
    public void setLookupValue(String lookupValue) { this.lookupValue = lookupValue; }
}
