package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PropertyResponse {
    private Long id;
    private String name;
    private String addressLine;
    private String ward;
    private String district;
    private String city;
    private String description;
    private LocalDateTime createdAt;
}