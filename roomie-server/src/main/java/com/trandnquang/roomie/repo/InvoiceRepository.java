package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Lấy tất cả hóa đơn của một hợp đồng
    List<Invoice> findByContractId(Long contractId);

    // Lấy danh sách hóa đơn CHƯA THANH TOÁN (Quan trọng để đòi nợ)
    List<Invoice> findByStatus(String status);

    // Lấy hóa đơn theo tháng/năm (Cần query phức tạp hơn chút, tạm thời dùng findByIssueDateBetween ở Service)
}