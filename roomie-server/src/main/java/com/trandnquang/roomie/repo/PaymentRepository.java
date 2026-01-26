package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Xem lịch sử trả tiền của hóa đơn X
    List<Payment> findByInvoiceId(Long invoiceId);
}