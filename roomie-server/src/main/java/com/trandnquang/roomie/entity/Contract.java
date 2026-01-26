package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE contract SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal deposit;

    @Column(name = "occupant_count")
    @Builder.Default
    private Integer occupantCount = 1;

    @Column(name = "rent_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal rentPrice;

    @Column(name = "contract_date")
    @Builder.Default
    private LocalDate contractDate = LocalDate.now();

    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // Quan hệ với cư dân, dịch vụ đăng ký, hóa đơn
    @OneToMany(mappedBy = "contract", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Resident> residents;

    @OneToMany(mappedBy = "contract", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ServiceSubscription> subscriptions;

    @OneToMany(mappedBy = "contract", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Invoice> invoices;
}