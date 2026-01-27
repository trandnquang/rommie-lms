package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "resident")
@SQLDelete(sql = "UPDATE resident SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Resident extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private String fullName;
    private String phoneNumber;
    private String identityCardNumber;

    @Builder.Default
    private boolean isContractHolder = false; // Check if this resident is the Tenant

    private LocalDate moveInDate;
    private LocalDate moveOutDate;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;
}