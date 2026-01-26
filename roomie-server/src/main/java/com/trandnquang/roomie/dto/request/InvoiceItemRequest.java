package com.trandnquang.roomie.dto.request;
import lombok.Data;

@Data
public class InvoiceItemRequest {
    private Long serviceId;
    private Integer oldValue; // Chỉ số cũ
    private Integer newValue; // Chỉ số mới (Hệ thống sẽ tự trừ ra số dùng)
}