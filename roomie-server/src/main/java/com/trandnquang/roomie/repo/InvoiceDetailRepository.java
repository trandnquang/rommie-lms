package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    List<InvoiceDetail> findByInvoiceId(Long invoiceId);

    // Tìm chi tiết của tháng trước để lấy 'newIndex' làm 'oldIndex' cho tháng này
    Optional<InvoiceDetail> findByInvoiceIdAndUtilityId(Long invoiceId, Long utilityId);
}