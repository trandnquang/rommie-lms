package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.dto.utility.TierConfig;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "contract_utility")
// Vẫn dùng Soft Delete để giữ lịch sử nếu lỡ tay xóa dịch vụ
@SQLDelete(sql = "UPDATE contract_utility SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContractUtility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utility_id", nullable = false)
    private Utility utility;

    // --- SNAPSHOT FIELDS (PRICE LOCKING) ---

    // Giá chốt (Dùng cho FIXED hoặc FLAT tại thời điểm ký)
    // Nếu Utility đổi giá sau này, cột này vẫn giữ giá cũ.
    @Column(name = "locked_price", precision = 15, scale = 2)
    private BigDecimal lockedPrice;

    // Cấu hình bậc thang chốt (Dùng cho TIERED tại thời điểm ký)
    // Copy nguyên mảng JSON từ Utility sang đây.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "locked_tier_config", columnDefinition = "jsonb")
    private List<TierConfig> lockedTierConfig;

    // ---------------------------------------

    @Builder.Default
    private Integer amount = 1; // Số lượng (ví dụ: 2 người, 1 xe)

    @Column(name = "start_index", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal startIndex = BigDecimal.ZERO; // Chỉ số đầu vào (cho điện/nước)

    // QUAN TRỌNG: Đã xóa field 'isDeleted'
}