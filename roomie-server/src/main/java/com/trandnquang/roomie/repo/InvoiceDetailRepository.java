package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

    // Retrieve line items for a specific invoice
    List<InvoiceDetail> findByInvoiceId(Long invoiceId);
}