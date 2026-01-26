package com.trandnquang.roomie.dto.request;
import lombok.Data;

@Data
public class PropertyRequest {
    private String name;
    private String addressLine;
    private String ward;
    private String district;
    private String city;
    private String description;
}