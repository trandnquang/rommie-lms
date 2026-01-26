package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    // Tìm khách theo CCCD (Quan trọng để check trùng khách cũ)
    Optional<Tenant> findByIdentityNumber(String identityNumber);

    // Tìm khách theo Tên (Search box)
    List<Tenant> findByFullNameContainingIgnoreCase(String name);

    // Tìm khách theo số điện thoại
    Optional<Tenant> findByPhoneNumber(String phoneNumber);
}