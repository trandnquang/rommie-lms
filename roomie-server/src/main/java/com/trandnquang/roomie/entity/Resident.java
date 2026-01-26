package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant; // Resident cũng là 1 Tenant (về mặt thông tin cá nhân)

    @Builder.Default
    private String role = "MEMBER";

    @Column(name = "move_in_date")
    @Builder.Default
    private LocalDate moveInDate = LocalDate.now();

    @Column(name = "move_out_date")
    private LocalDate moveOutDate;
}