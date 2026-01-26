package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(name = "payment_date")
    @Builder.Default
    private LocalDate paymentDate = LocalDate.now();

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 100)
    private String method; // CASH, BANK_TRANSFER
}