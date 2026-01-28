package com.trandnquang.roomie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trandnquang.roomie.model.enums.UserRole;
import com.trandnquang.roomie.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
// Kích hoạt Soft Delete cho User (Rất quan trọng vì User liên quan đến Audit log)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    /**
     * SECURITY NOTE:
     * 1. @JsonIgnore: Ngăn password bị lộ ra API nếu lỡ return Entity.
     * 2. @ToString.Exclude: Ngăn password bị ghi vào Log server.
     */
    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 20)
    private UserRole role = UserRole.ROLE_MANAGER;
}