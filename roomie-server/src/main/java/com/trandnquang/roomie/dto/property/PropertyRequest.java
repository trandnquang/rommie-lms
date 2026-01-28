package com.trandnquang.roomie.dto.property;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRequest {
    @NotBlank(message = "Property name is required")
    private String name;

    private String description;

    @NotBlank(message = "Address line is required")
    private String addressLine;

    private String ward;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "City is required")
    private String city;
}