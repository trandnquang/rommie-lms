package com.trandnquang.roomie.dto.room;

import com.trandnquang.roomie.model.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @PositiveOrZero(message = "Floor number must be positive or zero")
    private Integer floorNumber;

    @Positive(message = "Area must be positive")
    private BigDecimal area; // Changed from Double to BigDecimal

    @NotNull(message = "Base price is required")
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal basePrice;

    @Positive(message = "Capacity must be at least 1")
    private Integer capacity;

    private String imageUrl;

    private RoomStatus status; // Optional, default AVAILABLE in Service
}