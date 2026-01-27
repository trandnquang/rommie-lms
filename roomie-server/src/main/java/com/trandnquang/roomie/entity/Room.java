package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Table(name = "room")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    private String roomNumber;
    private Integer floorNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(precision = 15, scale = 2)
    private BigDecimal basePrice;
    private Integer capacity;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;
}