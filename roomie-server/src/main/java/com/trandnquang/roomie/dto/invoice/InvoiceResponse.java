package com.trandnquang.roomie.dto.invoice;

import com.trandnquang.roomie.model.enums.InvoiceStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class InvoiceResponse {
    private Long id;
    private String invoiceCode;

    private String roomNumber;
    private String tenantName;

    private Integer month;
    private Integer year;
    private LocalDate issueDate;
    private LocalDate dueDate;

    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;

    private InvoiceStatus status;

    private List<InvoiceDetailResponse> details; // Chi tiết từng dòng tiền
}