package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "unit")
public class Unit {

    @Id
    private Integer unitID;

    private String unitName;
    private Integer districtID;
    private Integer typeID;

    public Unit() {}

    public Unit(Integer unitID, String unitName, Integer districtID, Integer typeID) {
        this.unitID = unitID;
        this.unitName = unitName;
        this.districtID = districtID;
        this.typeID = typeID;
    }

    public Integer getUnitID() { return unitID; }
    public void setUnitID(Integer unitID) { this.unitID = unitID; }

    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    public Integer getDistrictID() { return districtID; }
    public void setDistrictID(Integer districtID) { this.districtID = districtID; }

    public Integer getTypeID() { return typeID; }
    public void setTypeID(Integer typeID) { this.typeID = typeID; }
}
