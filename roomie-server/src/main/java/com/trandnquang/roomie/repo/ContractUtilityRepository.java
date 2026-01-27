package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.ContractUtility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractUtilityRepository extends JpaRepository<ContractUtility, Long> {

    // CRITICAL: Get registered services for a contract to calculate Bill
    List<ContractUtility> findByContractId(Long contractId);
}