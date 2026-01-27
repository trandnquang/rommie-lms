package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.ContractStatus;
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
@Table(name = "contract")
@SQLDelete(sql = "UPDATE contract SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(unique = true)
    private String contractCode;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualEndDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal depositAmount;
    @Column(precision = 15, scale = 2)
    private BigDecimal rentPrice;

    @Builder.Default
    private Integer paymentCycle = 1;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ContractStatus status = ContractStatus.ACTIVE;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Resident> residents = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<ContractUtility> contractUtilities = new ArrayList<>();

    public void addResident(Resident resident) {
        this.residents.add(resident);
        resident.setContract(this);
    }

    public void addContractService(ContractUtility service) {
        this.contractUtilities.add(service);
        service.setContract(this);
    }
}