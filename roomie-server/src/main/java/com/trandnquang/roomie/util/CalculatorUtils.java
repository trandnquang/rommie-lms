package com.trandnquang.roomie.util;

import com.trandnquang.roomie.dto.utility.TierConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculatorUtils {

    // Helper: Cộng
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b);
    }

    // Helper: Trừ
    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).subtract(b == null ? BigDecimal.ZERO : b);
    }

    // Helper: Nhân
    public static BigDecimal mul(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).multiply(b == null ? BigDecimal.ZERO : b);
    }

    // Helper: Nhân tiền (Amount * Price)
    public static BigDecimal calculateCost(BigDecimal amount, BigDecimal price) {
        return mul(amount, price).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * LOGIC TÍNH TIỀN BẬC THANG (TIERED PRICING)
     * @param usage: Số tiêu thụ (VD: 120 kWh)
     * @param configs: Cấu hình bậc thang (List từ JSON)
     * @return Tổng tiền
     */
    public static BigDecimal calculateTieredCost(BigDecimal usage, List<TierConfig> configs) {
        if (usage == null || usage.compareTo(BigDecimal.ZERO) <= 0 || configs == null || configs.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal remainingUsage = usage;

        // Sắp xếp bậc thang từ thấp đến cao (đề phòng JSON lộn xộn)
        configs.sort((a, b) -> a.getMin().compareTo(b.getMin()));

        for (TierConfig tier : configs) {
            if (remainingUsage.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal min = BigDecimal.valueOf(tier.getMin());
            BigDecimal max = BigDecimal.valueOf(tier.getMax());

            // Tính độ rộng của bậc này (VD: bậc 0-50 -> rộng 50)
            BigDecimal tierRange = max.subtract(min).add(BigDecimal.ONE); // +1 vì bao gồm cả min

            // Nếu max quá lớn (vô cực/bậc cuối), coi như tierRange là phần còn lại
            if (tier.getMax() >= 999999) { // Giả sử 999999 là max vô cực
                tierRange = remainingUsage;
            }

            // Lấy min giữa (Phần còn lại) và (Độ rộng bậc)
            BigDecimal usageInThisTier = remainingUsage.min(tierRange);

            // Cộng tiền: Số dùng ở bậc này * Giá bậc này
            totalCost = totalCost.add(usageInThisTier.multiply(tier.getPrice()));

            // Trừ đi số điện đã tính
            remainingUsage = remainingUsage.subtract(usageInThisTier);
        }

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }
}