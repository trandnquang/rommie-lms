package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.payment.PaymentRequest;
import com.trandnquang.roomie.dto.payment.PaymentResponse;
import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.entity.Payment;
import com.trandnquang.roomie.model.enums.InvoiceStatus;
import com.trandnquang.roomie.repo.InvoiceRepository;
import com.trandnquang.roomie.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {
        // 1. Validate
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found ID: " + request.getInvoiceId()));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new RuntimeException("Cannot pay for a CANCELLED invoice.");
        }

        // 2. Create Entity
        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionCode(request.getTransactionCode())
                .paymentDate(LocalDateTime.now())
                .note(request.getNote())
                .build();

        // 3. Update Invoice Status
        BigDecimal currentPaid = invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount();
        BigDecimal newPaidAmount = currentPaid.add(request.getAmount());

        invoice.setPaidAmount(newPaidAmount);

        // Logic so sánh: Nếu (Đã trả) >= (Tổng tiền - Sai số cho phép rất nhỏ) -> PAID
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        invoiceRepository.save(invoice);
        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    public List<PaymentResponse> getHistoryByInvoice(Long invoiceId) {
        List<Payment> payments = paymentRepository.findByInvoiceId(invoiceId);
        return payments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Mapper Helper
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .invoiceId(payment.getInvoice().getId())
                .invoiceCode(payment.getInvoice().getInvoiceCode())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .transactionCode(payment.getTransactionCode())
                .paymentDate(payment.getPaymentDate())
                .note(payment.getNote())
                .build();
    }
}