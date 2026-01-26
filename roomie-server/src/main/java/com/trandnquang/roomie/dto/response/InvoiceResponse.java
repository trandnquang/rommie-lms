package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceResponse {
    private Long id;
    private String roomNumber;
    private String tenantName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private String status; // UNPAID / PAID
}