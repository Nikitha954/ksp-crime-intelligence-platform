package com.example.demo.repository;

import com.example.demo.entity.CaseMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CaseMasterRepository extends JpaRepository<CaseMaster, Long>, JpaSpecificationExecutor<CaseMaster> {

    List<CaseMaster> findByDistrictNameContainingIgnoreCase(String districtName);

    List<CaseMaster> findByCrimeSubHeadNameContainingIgnoreCase(String crimeSubHeadName);

    @Query("SELECT c.crimeSubHeadName as label, COUNT(c) FROM CaseMaster c GROUP BY c.crimeSubHeadName ORDER BY COUNT(c) DESC")
    List<Object[]> countByCrimeSubHead();

    @Query("SELECT c.districtName as label, COUNT(c) FROM CaseMaster c GROUP BY c.districtName ORDER BY COUNT(c) DESC")
    List<Object[]> countByDistrict();

    @Query("SELECT EXTRACT(YEAR FROM c.crimeRegisteredDate) as yr, EXTRACT(MONTH FROM c.crimeRegisteredDate) as mth, COUNT(c) as count " +
           "FROM CaseMaster c " +
           "GROUP BY EXTRACT(YEAR FROM c.crimeRegisteredDate), EXTRACT(MONTH FROM c.crimeRegisteredDate) " +
           "ORDER BY yr ASC, mth ASC")
    List<Object[]> countByMonth();

    @Query("SELECT c.districtID, c.districtName, c.policeStationID, c.policeStationName, AVG(c.latitude), AVG(c.longitude), COUNT(c), " +
           "SUM(CASE WHEN c.crimeRegisteredDate >= :recentDate THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN c.crimeRegisteredDate >= :rollingDate THEN 1 ELSE 0 END) " +
           "FROM CaseMaster c GROUP BY c.districtID, c.districtName, c.policeStationID, c.policeStationName")
    List<Object[]> getHotspotAggregates(@Param("recentDate") LocalDate recentDate, @Param("rollingDate") LocalDate rollingDate);

    List<CaseMaster> findByCaseMasterIDIn(List<Long> caseMasterIds);
}
