package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
// Soft Delete Standard
@SQLDelete(sql = "UPDATE invoice SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "invoice_code", unique = true, nullable = false)
    private String invoiceCode;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", precision = 15, scale = 2)
    @Builder.Default
    @ColumnDefault("0")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * GENERATED COLUMN STRATEGY:
     * Cột này được tính toán bởi Database (total_amount - paid_amount).
     * @Generated(event = { EventType.INSERT, EventType.UPDATE }): Báo cho Hibernate refresh giá trị này sau mỗi lệnh Insert và Update.
     */
    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "remaining_amount", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    // Relationships
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    // Helper Methods
    public void addDetail(InvoiceDetail detail) {
        this.details.add(detail);
        detail.setInvoice(this);
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setInvoice(this);
    }

    // IMPORTANT: removed 'isDeleted' field
}