package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
@SQLDelete(sql = "UPDATE invoice SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Invoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Column(unique = true)
    private String invoiceCode;

    private Integer month;
    private Integer year;

    private LocalDate issueDate;
    private LocalDate dueDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount;
    @Column(precision = 15, scale = 2)
    private BigDecimal paidAmount;

    @Column(insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    // ... (Giữ nguyên phần Relationships và Helper Methods như cũ)
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<InvoiceDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    public void addDetail(InvoiceDetail detail) {
        this.details.add(detail);
        detail.setInvoice(this);
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setInvoice(this);
    }
}