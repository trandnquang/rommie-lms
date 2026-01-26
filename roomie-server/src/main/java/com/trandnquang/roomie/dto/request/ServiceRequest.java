package com.trandnquang.roomie.dto.request;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceRequest {
    private String name;
    private BigDecimal price;
    private String unit; // kWh, m3, month
}