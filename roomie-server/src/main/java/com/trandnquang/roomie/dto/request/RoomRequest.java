package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomRequest {
    private Long propertyId;      // Chỉ cần ID nhà
    private String roomNumber;
    private Integer floorNumber;
    private BigDecimal area;
    private BigDecimal price;
    private Integer capacity;
    private String imageUrl;
}