package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.PaymentRequest;
import com.trandnquang.roomie.dto.response.PaymentResponse;
import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.entity.Payment;
import com.trandnquang.roomie.repo.InvoiceRepository;
import com.trandnquang.roomie.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setMethod(request.getMethod());

        Payment saved = paymentRepository.save(payment);

        // Update Invoice Status -> PAID
        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return mapToResponse(saved);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setInvoiceId(payment.getInvoice().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        response.setMethod(payment.getMethod());
        return response;
    }
}