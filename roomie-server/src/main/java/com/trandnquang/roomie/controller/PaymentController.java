package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.payment.PaymentRequest;
import com.trandnquang.roomie.dto.payment.PaymentResponse;
import com.trandnquang.roomie.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * TẠO THANH TOÁN (THU TIỀN)
     * Manager nhập số tiền khách đóng -> Hệ thống trừ nợ và update trạng thái hóa đơn.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return new ResponseEntity<>(paymentService.makePayment(request), HttpStatus.CREATED);
    }

    /**
     * XEM LỊCH SỬ THANH TOÁN CỦA 1 HÓA ĐƠN
     * (Ví dụ: Hóa đơn 1tr, khách trả 2 lần 500k -> API này trả về 2 dòng đó)
     */
    @GetMapping("/invoice/{invoiceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TENANT')")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getHistoryByInvoice(invoiceId));
    }
}