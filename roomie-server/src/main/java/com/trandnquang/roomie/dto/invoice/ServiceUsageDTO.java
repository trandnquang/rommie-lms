package com.trandnquang.roomie.dto.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUsageDTO {
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "New index value is required")
    private Double newIndex; // Chỉ số mới
}