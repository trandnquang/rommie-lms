package com.trandnquang.roomie.model.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class District { // Đại diện cho Quận/Huyện
    private String name;
    private String code;
    @JsonProperty("unit_type")
    private String unitType;
    private List<Ward> wards;
}