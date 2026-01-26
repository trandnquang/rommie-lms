package com.trandnquang.roomie.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String unit;
}