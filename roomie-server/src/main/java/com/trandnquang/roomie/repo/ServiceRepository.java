package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    // Tìm dịch vụ theo tên (VD: check xem đã có dịch vụ "Wifi" chưa)
    Optional<Service> findByName(String name);
}