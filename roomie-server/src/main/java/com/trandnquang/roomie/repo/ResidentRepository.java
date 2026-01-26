package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    // Lấy danh sách người đang ở trong một hợp đồng
    List<Resident> findByContractId(Long contractId);

    // Kiểm tra xem Tenant A có đang là resident ở đâu đó không
    boolean existsByTenantId(Long tenantId);
}