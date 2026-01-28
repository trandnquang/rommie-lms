package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

    // Retrieve line items for a specific invoice
    List<InvoiceDetail> findByInvoiceId(Long invoiceId);

    // Tìm chi tiết hóa đơn của 1 dịch vụ cụ thể trong 1 hóa đơn (để lấy chỉ số mới làm chỉ số cũ cho tháng sau)
    Optional<InvoiceDetail> findByInvoiceIdAndUtilityId(Long invoiceId, Long utilityId);
}