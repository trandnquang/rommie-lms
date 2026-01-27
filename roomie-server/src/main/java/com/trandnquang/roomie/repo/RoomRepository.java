package com.trandnquang.roomie.repo;


import com.trandnquang.roomie.entity.Room;
import com.trandnquang.roomie.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByPropertyId(Long propertyId);

    // FIX: String status -> RoomStatus status
    List<Room> findByPropertyIdAndStatus(Long propertyId, RoomStatus status);

    List<Room> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // FIX: JPQL với Enum.
    // Cách 1: Truyền tham số vào (Clean & Dynamic)
    @Query("SELECT COUNT(r) FROM Room r WHERE r.property.id = :propertyId AND r.status = :status")
    long countByPropertyAndStatus(@Param("propertyId") Long propertyId, @Param("status") RoomStatus status);
}