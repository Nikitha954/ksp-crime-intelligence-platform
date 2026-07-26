package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "arrest_surrender")
public class ArrestSurrender {

    @Id
    private Long arrestSurrenderID;

    private Long caseMasterID;
    private Long accusedMasterID;
    private Integer arrestSurrenderTypeID;
    private LocalDate arrestSurrenderDate;
    private Integer policeStationID;
    private String ioid;

    public ArrestSurrender() {}

    public Long getArrestSurrenderID() { return arrestSurrenderID; }
    public void setArrestSurrenderID(Long arrestSurrenderID) { this.arrestSurrenderID = arrestSurrenderID; }

    public Long getCaseMasterID() { return caseMasterID; }
    public void setCaseMasterID(Long caseMasterID) { this.caseMasterID = caseMasterID; }

    public Long getAccusedMasterID() { return accusedMasterID; }
    public void setAccusedMasterID(Long accusedMasterID) { this.accusedMasterID = accusedMasterID; }

    public Integer getArrestSurrenderTypeID() { return arrestSurrenderTypeID; }
    public void setArrestSurrenderTypeID(Integer arrestSurrenderTypeID) { this.arrestSurrenderTypeID = arrestSurrenderTypeID; }

    public LocalDate getArrestSurrenderDate() { return arrestSurrenderDate; }
    public void setArrestSurrenderDate(LocalDate arrestSurrenderDate) { this.arrestSurrenderDate = arrestSurrenderDate; }

    public Integer getPoliceStationID() { return policeStationID; }
    public void setPoliceStationID(Integer policeStationID) { this.policeStationID = policeStationID; }

    public String getIoid() { return ioid; }
    public void setIoid(String ioid) { this.ioid = ioid; }
}
