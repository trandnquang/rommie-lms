package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.payment.PaymentRequest;
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

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public Payment makePayment(PaymentRequest request) {
        // 1. Validate Hóa đơn
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + request.getInvoiceId()));

        // Check nếu hóa đơn đã hủy hoặc đã trả hết
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new RuntimeException("Hóa đơn này đã bị hủy, không thể thanh toán.");
        }

        // (Optional) Check nếu trả thừa tiền quá nhiều?
        // Logic ở đây tôi cho phép trả thừa (để làm số dư cho tháng sau - credit),
        // nhưng Status tối đa chỉ là PAID.

        // 2. Tạo bản ghi Payment
        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionCode(request.getTransactionCode())
                .paymentDate(LocalDateTime.now())
                .note(request.getNote())
                .build();

        // 3. Logic cập nhật Hóa đơn
        // Cộng dồn tiền đã trả: paidAmount = paidAmount + requestAmount
        BigDecimal currentPaid = invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount();
        BigDecimal newPaidAmount = currentPaid.add(request.getAmount());

        invoice.setPaidAmount(newPaidAmount);

        // 4. Logic cập nhật Trạng thái (Auto Status)
        // Nếu (Tiền đã trả >= Tổng tiền) -> PAID
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            // Nếu chưa trả đủ nhưng > 0 -> PARTIAL
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        // 5. Lưu xuống DB
        invoiceRepository.save(invoice); // Lưu invoice trước để cập nhật status/paid
        return paymentRepository.save(payment); // Lưu payment
    }

    // Lấy lịch sử thanh toán của 1 hóa đơn
    public List<Payment> getHistoryByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }
}