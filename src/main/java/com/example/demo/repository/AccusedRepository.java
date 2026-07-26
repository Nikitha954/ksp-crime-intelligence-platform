package com.example.demo.repository;

import com.example.demo.entity.Accused;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccusedRepository extends JpaRepository<Accused, Long> {
    List<Accused> findByPersonID(String personID);
    List<Accused> findByCaseMasterID(Long caseMasterID);
    List<Accused> findByCaseMasterIDIn(List<Long> caseMasterIds);
}
