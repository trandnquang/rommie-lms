package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Tìm kiếm khu trọ theo tên (VD: gõ "Green" ra "Green House") - Không phân biệt hoa thường
    List<Property> findByNameContainingIgnoreCase(String name);

    // Lọc khu trọ theo thành phố
    List<Property> findByCity(String city);
}
