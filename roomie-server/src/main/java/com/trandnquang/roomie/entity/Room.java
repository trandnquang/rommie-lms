package com.trandnquang.roomie.entity;

import com.trandnquang.roomie.model.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "room")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false) // Room bắt buộc phải thuộc về Property
    private Property property;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "base_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal basePrice;

    private Integer capacity;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    // QUAN TRỌNG: Đã xóa field 'isDeleted' tại đây
}