package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {

    // List all people living under a specific contract
    List<Resident> findByContractId(Long contractId);

    // Check if a person is already resident elsewhere (by ID Card)
    Optional<Resident> findByIdentityCardNumber(String identityCardNumber);
}