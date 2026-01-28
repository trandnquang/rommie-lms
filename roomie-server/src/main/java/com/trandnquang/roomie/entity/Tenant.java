package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "tenant")
// Soft Delete Standard
@SQLDelete(sql = "UPDATE tenant SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CCCD là bắt buộc và duy nhất
    @Column(name = "identity_card_number", unique = true, nullable = false, length = 20)
    private String identityCardNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    private String email;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    // Địa chỉ thường trú (trên CCCD)
    @Column(name = "address_line")
    private String addressLine;

    private String ward;
    private String district;
    private String city;

    // QUAN TRỌNG: Đã xóa field 'isDeleted'
}