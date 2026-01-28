package com.trandnquang.roomie.dto.payment;

import com.trandnquang.roomie.model.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private Long invoiceId;         // Chỉ trả về ID, không trả cả object Invoice
    private String invoiceCode;     // Kèm mã hóa đơn để dễ tra cứu
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String transactionCode;
    private LocalDateTime paymentDate;
    private String note;
}