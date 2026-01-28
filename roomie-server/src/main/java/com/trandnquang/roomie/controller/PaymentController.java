package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.payment.PaymentRequest;
import com.trandnquang.roomie.entity.Payment;
import com.trandnquang.roomie.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // API 5.1: Tạo thanh toán
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.makePayment(request));
    }

    // API 5.2: Xem lịch sử thanh toán của hóa đơn
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> getPaymentHistory(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getHistoryByInvoice(invoiceId));
    }
}