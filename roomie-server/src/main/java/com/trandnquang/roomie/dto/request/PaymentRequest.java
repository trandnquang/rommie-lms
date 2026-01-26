package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method; // CASH / BANKING
    private String note;
}