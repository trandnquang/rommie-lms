package com.trandnquang.roomie.dto.room;

import com.trandnquang.roomie.model.enums.RoomStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Integer floorNumber;
    private Double area;
    private BigDecimal basePrice;
    private Integer capacity;
    private String imageUrl;
    private RoomStatus status;

    // Flatten Data: Chỉ trả về tên Property thay vì cả object Property to đùng
    private Long propertyId;
    private String propertyName;
    private String fullAddress;
}