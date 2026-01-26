package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Lấy danh sách phòng thuộc một nhà cụ thể
    List<Room> findByPropertyId(Long propertyId);

    // Tìm tất cả phòng đang TRỐNG (AVAILABLE) để giới thiệu khách
    List<Room> findByStatus(String status);

    // Tìm phòng trống trong một nhà cụ thể
    List<Room> findByPropertyIdAndStatus(Long propertyId, String status);

    // Tìm theo số phòng (VD: tìm phòng "101")
    List<Room> findByRoomNumber(String roomNumber);
}