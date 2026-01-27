package com.trandnquang.roomie.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "property")
@SQLDelete(sql = "UPDATE property SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Property extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Location Details
    private String addressLine;
    private String ward;
    private String district;
    private String city;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    // Relationship: One Property -> Many Rooms
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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