package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "contract_utility")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class ContractUtility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utility_id")
    private Utility utility;

    @Builder.Default
    private Integer amount = 1; // Quantity (e.g., 2 motorbikes)

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal startIndex = BigDecimal.ZERO; // Initial meter reading for electricity/water
}