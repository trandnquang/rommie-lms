package com.trandnquang.roomie.repo;


import com.trandnquang.roomie.entity.Contract;
import com.trandnquang.roomie.model.enums.ContractStatus; // Import Enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByContractCode(String contractCode);

    // FIX: String status -> ContractStatus status
    Optional<Contract> findByRoomIdAndStatus(Long roomId, ContractStatus status);

    List<Contract> findByTenantId(Long tenantId);

    // FIX: Dùng cho Batch Job tính tiền
    List<Contract> findByStatus(ContractStatus status);

    // FIX: JPQL query
    // Lưu ý: Khi so sánh trong @Query, tốt nhất là truyền Enum vào làm tham số (:status)
    @Query("SELECT c FROM Contract c WHERE c.endDate BETWEEN :startDate AND :endDate AND c.status = :status")
    List<Contract> findExpiringContracts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ContractStatus status
    );
}