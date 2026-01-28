package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.dto.utility.TierConfig;
import com.trandnquang.roomie.model.enums.PricingType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utility")
@SQLDelete(sql = "UPDATE utility SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Utility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private PricingType pricingType = PricingType.FLAT;

    private String unit; // e.g., kWh, m3, Person, Room

    // Giá cơ bản (Dùng cho FIXED hoặc FLAT)
    @Column(name = "base_price", precision = 15, scale = 2)
    private BigDecimal basePrice;

    private String description;

    /**
     * Cấu hình giá lũy tiến (Dùng cho TIERED).
     * Hibernate 6 tự động map List<Object> thành JSON Array trong DB.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tier_config", columnDefinition = "jsonb")
    @Builder.Default
    private List<TierConfig> tierConfig = new ArrayList<>();

    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;

    // QUAN TRỌNG: Đã xóa field 'isDeleted'
}