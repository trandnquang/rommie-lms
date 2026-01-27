package com.trandnquang.roomie.dto.contract;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractUtilityRequest {
    private Long utilityId;
    private Integer amount; // Số lượng (VD: 2 xe máy)
    private BigDecimal startIndex; // Chỉ số đầu (Điện/Nước) khi bàn giao phòng
}