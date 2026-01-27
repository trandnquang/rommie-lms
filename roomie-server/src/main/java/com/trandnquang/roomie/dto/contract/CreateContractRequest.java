package com.trandnquang.roomie.dto.contract;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateContractRequest {
    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId; // Người đứng tên hợp đồng

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Rent price is required")
    private BigDecimal rentPrice; // Giá chốt

    private BigDecimal depositAmount;
    private Integer paymentCycle; // 1 tháng đóng 1 lần

    // Danh sách cư dân (Bao gồm cả chủ HĐ nếu họ cũng ở đó)
    @NotEmpty(message = "At least one resident is required")
    private List<ResidentRequest> residents;

    // Danh sách dịch vụ đăng ký
    private List<ContractUtilityRequest> services;
}