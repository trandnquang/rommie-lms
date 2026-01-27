package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Filter properties by location (Search feature)
    List<Property> findByCityAndDistrict(String city, String district);

    // Note: 'isDeleted' filter is handled automatically by @Where in Entity
}