package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE tenant SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identity_number", nullable = false, unique = true, length = 20)
    private String identityNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(name = "address_line")
    private String addressLine;

    private String ward;
    private String district;
    private String city;

    @Column(length = 10)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // Tenant có thể có nhiều hợp đồng (Lịch sử thuê)
    @OneToMany(mappedBy = "tenant", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Contract> contracts;
}