package com.trandnquang.roomie.dto.contract;

import com.trandnquang.roomie.dto.resident.ResidentRequest;
import com.trandnquang.roomie.dto.tenant.TenantRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateContractRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    // --- LOGIC: Chọn Tenant cũ HOẶC Tạo mới ---
    private Long tenantId; // Nếu chọn khách cũ

    @Valid
    private TenantRequest newTenant; // Nếu tạo khách mới (tenantId phải null)
    // -------------------------------------------

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Rent price is required")
    @Positive(message = "Rent price must be positive")
    private BigDecimal rentPrice;

    @PositiveOrZero(message = "Deposit amount cannot be negative")
    private BigDecimal depositAmount;

    private Integer paymentCycle = 1; // Default 1 month

    // Danh sách người ở cùng (Validate từng phần tử trong list)
    @Valid
    private List<ResidentRequest> residents;

    // Dịch vụ
    @Valid
    private List<ContractUtilityRequest> utilities;
}