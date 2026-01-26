package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    // Tìm user để login
    Optional<UserAccount> findByUsername(String username);

    // Kiểm tra user tồn tại chưa (khi tạo tài khoản mới)
    boolean existsByUsername(String username);
}