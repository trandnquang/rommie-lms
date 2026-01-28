package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "resident")
@SQLDelete(sql = "UPDATE resident SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Resident extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false) // Cư dân phải thuộc về 1 hợp đồng
    private Contract contract;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "identity_card_number")
    private String identityCardNumber;

    /**
     * True nếu người này chính là Tenant đứng tên hợp đồng.
     * Dùng để UI hiển thị label "Chủ phòng".
     */
    @Builder.Default
    @Column(name = "is_contract_holder")
    private boolean isContractHolder = false;

    @Column(name = "move_in_date", nullable = false)
    private LocalDate moveInDate;

    @Column(name = "move_out_date")
    private LocalDate moveOutDate;

    // QUAN TRỌNG: Đã xóa field 'isDeleted'
}