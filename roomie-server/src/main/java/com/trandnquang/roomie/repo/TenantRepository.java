package com.trandnquang.roomie.repo;


import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.model.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByIdentityCardNumber(String identityCardNumber);
    List<Tenant> findByFullNameContainingIgnoreCase(String fullName);
    Optional<Tenant> findByPhoneNumber(String phoneNumber);
    List<Tenant> findByGender(Gender gender);
}