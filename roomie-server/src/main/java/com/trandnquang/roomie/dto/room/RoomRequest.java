package com.trandnquang.roomie.dto.room;

import com.trandnquang.roomie.model.enums.RoomStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomRequest {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Min(value = 0, message = "Floor number must be positive")
    private Integer floorNumber;

    private Double area;

    @NotNull(message = "Base price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal basePrice;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private String imageUrl;

    private RoomStatus status; // Optional, default AVAILABLE
}