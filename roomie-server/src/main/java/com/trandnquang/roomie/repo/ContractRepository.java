package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // Tìm hợp đồng của một phòng đang chạy
    List<Contract> findByRoomIdAndStatus(Long roomId, String status);

    // Tìm tất cả hợp đồng của một khách
    List<Contract> findByTenantId(Long tenantId);

    // Tìm các hợp đồng sắp hết hạn trước ngày X (Để cảnh báo gia hạn)
    // Ví dụ: Tìm các hợp đồng ACTIVE sẽ hết hạn trước ngày 30/06
    List<Contract> findByStatusAndEndDateBefore(String status, LocalDate date);

    // Tìm hợp đồng đang ACTIVE tại phòng đó (Để chặn không cho tạo thêm HĐ mới trùng phòng)
    boolean existsByRoomIdAndStatus(Long roomId, String status);
}