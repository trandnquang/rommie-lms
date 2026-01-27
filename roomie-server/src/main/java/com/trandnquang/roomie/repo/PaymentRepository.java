package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Payment;
import com.trandnquang.roomie.model.enums.PaymentMethod; // Import Enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceId(Long invoiceId);

    // MỚI: Thống kê giao dịch theo phương thức thanh toán
    List<Payment> findByPaymentMethod(PaymentMethod method);
}