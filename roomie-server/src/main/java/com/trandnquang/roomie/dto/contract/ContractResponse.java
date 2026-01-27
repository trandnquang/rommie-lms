package com.trandnquang.roomie.dto.contract;

import com.trandnquang.roomie.model.enums.ContractStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ContractResponse {
    private Long id;
    private String contractCode;

    // Thông tin phòng tóm tắt
    private String roomNumber;
    private String propertyName;

    // Thông tin người thuê chính tóm tắt
    private String tenantName;
    private String tenantPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentPrice;
    private BigDecimal depositAmount;
    private ContractStatus status;

    // Chi tiết con
    private List<ResidentResponse> residents;
    private List<ContractUtilityResponse> services;
}