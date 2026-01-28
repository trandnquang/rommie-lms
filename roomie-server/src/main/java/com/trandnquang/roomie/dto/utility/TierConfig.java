package com.trandnquang.roomie.dto.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * POJO class to map JSONB data for Tiered Pricing.
 * Structure: [{"min": 0, "max": 50, "price": 1678}, ...]
 * Implements Serializable for Caching/Clustering compatibility.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Best practice for Serializable

    private Integer min;       // Chỉ số bắt đầu (e.g., 0)

    // Lưu ý: Nếu max = null nghĩa là bậc thang cuối (đến vô cùng)
    private Integer max;       // Chỉ số kết thúc (e.g., 50)

    private BigDecimal price;  // Giá tiền cho bậc này
}