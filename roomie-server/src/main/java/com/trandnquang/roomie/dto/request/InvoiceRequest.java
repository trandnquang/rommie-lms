package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceRequest {
    private Long contractId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    // Danh sách các dịch vụ sử dụng trong tháng (Điện, Nước...)
    private List<InvoiceItemRequest> items;
}