package com.example.demo.repository;

import com.example.demo.entity.ArrestSurrender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrestSurrenderRepository extends JpaRepository<ArrestSurrender, Long> {
    List<ArrestSurrender> findByAccusedMasterID(Long accusedMasterID);
    List<ArrestSurrender> findByCaseMasterID(Long caseMasterID);
}
