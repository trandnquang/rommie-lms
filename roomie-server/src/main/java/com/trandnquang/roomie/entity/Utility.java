package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.dto.utility.TierConfig;
import com.trandnquang.roomie.model.enums.PricingType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "utility")
@SQLDelete(sql = "UPDATE utility SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Utility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PricingType pricingType = PricingType.FLAT;

    private String unit;
    @Column(precision = 15, scale = 2)
    private BigDecimal basePrice;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON) // Báo cho Hibernate biết đây là kiểu JSON
    @Column(name = "tier_config", columnDefinition = "jsonb") // Mapping với cột jsonb trong PG
    private List<TierConfig> tierConfig;

    @Builder.Default
    private boolean isActive = true;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;
}