package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomResponse {
    private Long id;
    private String propertyName;  // Flatten: Hiển thị tên nhà thay vì object
    private String roomNumber;
    private Integer floorNumber;
    private BigDecimal area;
    private BigDecimal price;
    private Integer capacity;
    private String status;        // AVAILABLE / OCCUPIED
    private String imageUrl;
}