package com.trandnquang.roomie.dto.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierConfig {
    private Integer min; // Chỉ số bắt đầu (VD: 0)
    private Integer max; // Chỉ số kết thúc (VD: 50)
    private BigDecimal price; // Giá tiền cho bậc này
}