package com.example.demo.repository;

import com.example.demo.entity.ComplainantDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainantDetailsRepository extends JpaRepository<ComplainantDetails, Long> {
    List<ComplainantDetails> findByCaseMasterID(Long caseMasterID);
}
