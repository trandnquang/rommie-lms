package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    private Integer capacity;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Builder.Default
    private String status = "AVAILABLE";

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // Quan hệ 1-N với Contract
    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Contract> contracts;
}