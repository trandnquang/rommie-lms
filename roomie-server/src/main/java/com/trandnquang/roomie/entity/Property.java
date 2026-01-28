package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "property")
// Hibernate 6.3+ Standard for Soft Delete
@SQLDelete(sql = "UPDATE property SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Property extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    // Location Details
    @Column(name = "address_line")
    private String addressLine;
    private String ward;
    private String district;
    private String city;

    // QUAN TRỌNG: Đã xóa field 'isDeleted' tại đây vì đã có trong BaseEntity

    // Relationship: One Property -> Many Rooms
    // orphanRemoval = true: Nếu xóa Room khỏi list này, Room sẽ bị xóa khỏi DB (kết hợp Soft Delete)
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    // --- Helper Methods for Bidirectional Relationship ---
    public void addRoom(Room room) {
        this.rooms.add(room);
        room.setProperty(this);
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
        room.setProperty(null);
    }
}