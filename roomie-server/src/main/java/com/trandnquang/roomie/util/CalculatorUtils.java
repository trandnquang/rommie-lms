package com.trandnquang.roomie.util;

import com.trandnquang.roomie.dto.utility.TierConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CalculatorUtils {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    // --- Helper Methods (Null Safe) ---

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).subtract(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal mul(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).multiply(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal calculateCost(BigDecimal amount, BigDecimal price) {
        return mul(amount, price).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Validate chỉ số điện/nước.
     * @throws IllegalArgumentException nếu số mới nhỏ hơn số cũ.
     */
    public static BigDecimal calculateUsage(BigDecimal oldIndex, BigDecimal newIndex) {
        BigDecimal oldVal = oldIndex == null ? BigDecimal.ZERO : oldIndex;
        BigDecimal newVal = newIndex == null ? BigDecimal.ZERO : newIndex;

        if (newVal.compareTo(oldVal) < 0) {
            throw new IllegalArgumentException("Chỉ số mới (" + newVal + ") không được nhỏ hơn chỉ số cũ (" + oldVal + ")");
        }

        return newVal.subtract(oldVal);
    }

    /**
     * LOGIC TÍNH TIỀN BẬC THANG (TIERED PRICING) - CORE LOGIC
     * * Nguyên lý:
     * 1. Copy list config để không làm hỏng list gốc.
     * 2. Sort theo min tăng dần.
     * 3. Duyệt từng bậc, tính số điện nằm trong khoảng (quota) của bậc đó.
     * 4. Xử lý bậc vô cực (max == null).
     */
    public static BigDecimal calculateTieredCost(BigDecimal usage, List<TierConfig> configs) {
        if (usage == null || usage.compareTo(BigDecimal.ZERO) <= 0 || configs == null || configs.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal remainingUsage = usage;

        // 1. Tạo bản sao và sắp xếp (Tránh Side Effect lên list gốc)
        List<TierConfig> sortedConfigs = new ArrayList<>(configs);
        sortedConfigs.sort(Comparator.comparingInt(TierConfig::getMin));

        for (TierConfig tier : sortedConfigs) {
            if (remainingUsage.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal price = tier.getPrice();
            BigDecimal tierQuota; // Số lượng điện được tính giá ở bậc này

            // 2. Xác định Quota của bậc
            if (tier.getMax() == null) {
                // Bậc cuối cùng (Vô cực): Ôm trọn số lượng còn lại
                tierQuota = remainingUsage;
            } else {
                // Bậc thường: Tính dung lượng (Capacity) của bậc
                // Ví dụ: 0-50 -> Capacity = 50. (Logic: 50 - 0).
                // Lưu ý: Tùy nghiệp vụ nhập liệu là "0-50" hay "1-50".
                // Ở đây giả định nhập "0-50" nghĩa là 50 số đầu.
                BigDecimal min = BigDecimal.valueOf(tier.getMin());
                BigDecimal max = BigDecimal.valueOf(tier.getMax());

                // Công thức an toàn: Max - Min. (Nếu nhập 0-50 thì 50-0=50).
                // Nếu nhập 1-50 thì 50-1=49 (Sai). => Ta quy ước Min luôn bắt đầu là 0 cho bậc 1 trong UI.
                BigDecimal range = max.subtract(min);

                // Nếu Range <= 0 (Do cấu hình sai), ta bỏ qua bậc này để tránh lỗi
                if (range.compareTo(BigDecimal.ZERO) <= 0) continue;

                // Lấy phần nhỏ hơn giữa (Số còn lại) và (Quota bậc)
                tierQuota = remainingUsage.min(range);
            }

            // 3. Cộng tiền
            totalCost = totalCost.add(tierQuota.multiply(price));

            // 4. Trừ số điện đã tính
            remainingUsage = remainingUsage.subtract(tierQuota);
        }

        return totalCost.setScale(2, RoundingMode.HALF_UP);
    }
}