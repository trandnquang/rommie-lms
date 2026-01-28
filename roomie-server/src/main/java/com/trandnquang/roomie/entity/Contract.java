package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contract")
// Soft Delete Standard
@SQLDelete(sql = "UPDATE contract SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_code", unique = true, nullable = false)
    private String contractCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(name = "deposit_amount", precision = 15, scale = 2)
    private BigDecimal depositAmount;

    // PRICE LOCKING: Giá phòng tại thời điểm ký
    @Column(name = "rent_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal rentPrice;

    @Builder.Default
    @Column(name = "payment_cycle")
    private Integer paymentCycle = 1;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private ContractStatus status = ContractStatus.ACTIVE;

    // Relationships
    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Resident> residents = new ArrayList<>();

    // orphanRemoval = true: Xóa utility khỏi list contract sẽ xóa record trong DB
    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContractUtility> contractUtilities = new ArrayList<>();

    // Helper Methods
    public void addResident(Resident resident) {
        this.residents.add(resident);
        resident.setContract(this);
    }

    public void addContractUtility(ContractUtility utility) {
        this.contractUtilities.add(utility);
        utility.setContract(this);
    }

    // QUAN TRỌNG: Đã xóa field 'isDeleted'
}