package com.example.demo.repository;

import com.example.demo.entity.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VictimRepository extends JpaRepository<Victim, Long> {
    List<Victim> findByCaseMasterID(Long caseMasterID);
}
