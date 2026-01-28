package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Contract;
import com.trandnquang.roomie.model.enums.ContractStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    // PERFORMANCE: Dùng EntityGraph để fetch luôn Room và Tenant, tránh lỗi N+1
    @EntityGraph(attributePaths = {"room", "tenant"})
    List<Contract> findByTenantId(Long tenantId);

    // PERFORMANCE: Hàm này dùng cho Batch Job (tính tiền hàng tháng).
    // Phải fetch data liên quan và hỗ trợ Pageable để xử lý từng đợt (chunk processing).
    @EntityGraph(attributePaths = {"room", "tenant", "contractUtilities"})
    List<Contract> findByStatus(ContractStatus status, Pageable pageable);

    // Kiểm tra nhanh để validate
    boolean existsByRoomIdAndStatus(Long roomId, ContractStatus status);

    @Query("SELECT c FROM Contract c WHERE c.endDate BETWEEN :startDate AND :endDate AND c.status = :status")
    List<Contract> findExpiringContracts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ContractStatus status
    );
}