package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "tenant")
@SQLDelete(sql = "UPDATE tenant SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Tenant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identity_card_number", unique = true)
    private String identityCardNumber;

    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String addressLine;
    private String ward;
    private String district;
    private String city;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;
}