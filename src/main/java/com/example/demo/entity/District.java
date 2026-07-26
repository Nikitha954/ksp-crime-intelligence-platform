package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "district")
public class District {

    @Id
    private Integer districtID;

    private String districtName;
    private Integer stateID;

    public District() {}

    public District(Integer districtID, String districtName, Integer stateID) {
        this.districtID = districtID;
        this.districtName = districtName;
        this.stateID = stateID;
    }

    public Integer getDistrictID() { return districtID; }
    public void setDistrictID(Integer districtID) { this.districtID = districtID; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public Integer getStateID() { return stateID; }
    public void setStateID(Integer stateID) { this.stateID = stateID; }
}
