package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "old_value")
    @Builder.Default
    private Integer oldValue = 0;

    @Column(name = "new_value")
    @Builder.Default
    private Integer newValue = 0;

    // Lưu ý: Database của bạn dùng INT cho old/new nhưng tính tiền thì có thể cần nhân hệ số,
    // trong DB definition ở trên là INT, nhưng nếu muốn chính xác cho nước (VD: 10.5 m3) có thể đổi thành BigDecimal sau.
    // Ở đây mình map đúng theo SQL INT.

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "sub_total", precision = 15, scale = 2)
    private BigDecimal subTotal;

    @Column(columnDefinition = "TEXT")
    private String note;
}