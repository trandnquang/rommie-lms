package com.trandnquang.roomie.model.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ward { // Đại diện cho Phường/Xã
    private String name;
    private String code;
    @JsonProperty("unit_type")
    private String unitType;
}