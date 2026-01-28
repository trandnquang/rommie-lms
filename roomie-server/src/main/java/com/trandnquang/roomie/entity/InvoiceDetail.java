package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_detail")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetail extends BaseEntity { // Vẫn kế thừa BaseEntity để có Audit

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // Có thể null nếu là phí phạt hoặc phụ thu không nằm trong danh mục Utility
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utility_id")
    private Utility utility;

    @Column(name = "utility_name", nullable = false)
    private String utilityName;

    @Column(name = "old_index", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal oldIndex = BigDecimal.ZERO;

    @Column(name = "new_index", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal newIndex = BigDecimal.ZERO;

    /**
     * GENERATED COLUMN: (new_index - old_index)
     */
    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "usage_amount", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal usageAmount;

    @Column(name = "applied_price", precision = 15, scale = 2)
    private BigDecimal appliedPrice; // Giá chốt (cho FLAT/FIXED) hoặc Giá trung bình (cho TIERED)

    @Column(name = "total_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    private String description;
}