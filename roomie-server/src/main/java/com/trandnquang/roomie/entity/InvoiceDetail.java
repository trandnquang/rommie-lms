package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_detail")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class InvoiceDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    // Nullable if it's a custom fee/fine not in Service table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utility_id")
    private Utility utility;

    private String serviceName; // Snapshot of service name at billing time
    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal oldIndex = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal newIndex = BigDecimal.ZERO;

    // Generated Column: newIndex - oldIndex
    @Column(insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal usageAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal appliedPrice; // Price Snapshot (CRITICAL for data integrity)
    @Column(precision = 15, scale = 2)
    private BigDecimal totalPrice;

    private String description;
}