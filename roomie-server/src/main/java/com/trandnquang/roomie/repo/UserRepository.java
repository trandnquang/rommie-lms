package com.trandnquang.roomie.repo;


import com.trandnquang.roomie.entity.User;
import com.trandnquang.roomie.model.enums.UserRole;
import com.trandnquang.roomie.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByStatus(UserStatus status);
    List<User> findByRole(UserRole role);
}