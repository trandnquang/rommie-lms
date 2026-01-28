package com.trandnquang.roomie.repo;


import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.model.enums.InvoiceStatus; // Import Enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceCode(String invoiceCode);

    List<Invoice> findByContractIdOrderByMonthDesc(Long contractId);

    // Kiểm tra xem tháng này hợp đồng này đã có hóa đơn chưa
    boolean existsByContractIdAndMonthAndYear(Long contractId, Integer month, Integer year);

    // FIX: Tìm công nợ (Invoice có trạng thái chưa hoàn thành)
    // Thay vì viết Query phức tạp, Spring Data hỗ trợ "findByStatusIn"
    List<Invoice> findByStatusIn(List<InvoiceStatus> statuses);

    // Tìm tất cả hóa đơn đã thanh toán xong
    List<Invoice> findByStatus(InvoiceStatus status);

    // Tìm hóa đơn gần nhất trước tháng hiện tại để lấy chỉ số cũ
    @Query("SELECT i FROM Invoice i WHERE i.contract.id = :contractId AND (i.year < :year OR (i.year = :year AND i.month < :month)) ORDER BY i.year DESC, i.month DESC LIMIT 1")
    Optional<Invoice> findLatestInvoiceBefore(Long contractId, Integer month, Integer year);
}