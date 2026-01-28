package com.trandnquang.roomie.dto.contract;

import com.trandnquang.roomie.dto.resident.ResidentResponse;
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

    // Room Info
    private Long roomId; // Thêm ID để UI có thể link tới trang chi tiết phòng
    private String roomNumber;
    private String propertyName;
    private String address; // Địa chỉ nhà trọ

    // Tenant Info
    private Long tenantId;
    private String tenantName;
    private String tenantPhone;

    // Contract Details
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentPrice;
    private BigDecimal depositAmount;
    private Integer paymentCycle;
    private ContractStatus status;

    // Children
    private List<ResidentResponse> residents;
    private List<ContractUtilityResponse> utilities;
}