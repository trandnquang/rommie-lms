package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByContractId(Long contractId);

    // FIX: Trả về List để xem lịch sử cư trú của người này qua các hợp đồng khác nhau
    List<Resident> findByIdentityCardNumber(String identityCardNumber);
}