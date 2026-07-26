package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "case_category")
public class CaseCategory {

    @Id
    private Integer caseCategoryID;

    private String lookupValue;

    public CaseCategory() {}

    public CaseCategory(Integer caseCategoryID, String lookupValue) {
        this.caseCategoryID = caseCategoryID;
        this.lookupValue = lookupValue;
    }

    public Integer getCaseCategoryID() { return caseCategoryID; }
    public void setCaseCategoryID(Integer caseCategoryID) { this.caseCategoryID = caseCategoryID; }

    public String getLookupValue() { return lookupValue; }
    public void setLookupValue(String lookupValue) { this.lookupValue = lookupValue; }
}
