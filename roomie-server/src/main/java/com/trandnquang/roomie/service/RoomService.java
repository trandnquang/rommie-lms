package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.room.RoomRequest;
import com.trandnquang.roomie.dto.room.RoomResponse;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.entity.Room;
import com.trandnquang.roomie.model.enums.RoomStatus;
import com.trandnquang.roomie.repo.PropertyRepository;
import com.trandnquang.roomie.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;

    // --- 1. CREATE ROOM ---
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        // Bước 1: Tìm Property (Cha) trước
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Bước 2: Map Request -> Entity
        Room room = Room.builder()
                .property(property) // Gán quan hệ
                .roomNumber(request.getRoomNumber())
                .floorNumber(request.getFloorNumber())
                .area(request.getArea()) // Lưu ý convert Double -> BigDecimal
                .basePrice(request.getBasePrice())
                .capacity(request.getCapacity())
                .imageUrl(request.getImageUrl())
                .status(request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE)
                .isDeleted(false)
                .build();

        // Bước 3: Lưu
        Room savedRoom = roomRepository.save(room);

        // Bước 4: Map Entity -> Response (Gọi hàm private bên dưới)
        return mapToResponse(savedRoom);
    }

    // --- 2. GET ALL ROOMS ---
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::mapToResponse) // Method Reference
                .collect(Collectors.toList());
    }

    // --- 3. GET ROOMS BY PROPERTY ---
    public List<RoomResponse> getRoomsByProperty(Long propertyId) {
         List<Room> rooms = roomRepository.findByPropertyId(propertyId);
         return rooms.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // --- 4. UPDATE PRICE & INFO ---
    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomNumber(request.getRoomNumber());
        room.setBasePrice(request.getBasePrice());
        room.setCapacity(request.getCapacity());
        // Nếu muốn đổi tòa nhà thì phải set lại Property, ở đây tôi tạm bỏ qua

        Room updatedRoom = roomRepository.save(room);
        return mapToResponse(updatedRoom);
    }

    // --- 5. UPDATE STATUS (API riêng cho nhanh) ---
    @Transactional
    public void updateRoomStatus(Long id, RoomStatus status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus(status);
        roomRepository.save(room);
    }

    // =================================================================
    // PRIVATE HELPER: MANUAL MAPPING (Entity -> DTO)
    // =================================================================
    private RoomResponse mapToResponse(Room room) {
        // Flattening Data: Lấy thông tin từ bảng cha (Property) đưa ra ngoài
        String propName = room.getProperty().getName();
        String fullAddr = room.getProperty().getAddressLine() + ", " +
                          room.getProperty().getDistrict() + ", " +
                          room.getProperty().getCity();

        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .floorNumber(room.getFloorNumber())
                .area(room.getArea().doubleValue()) // BigDecimal -> Double
                .basePrice(room.getBasePrice())
                .capacity(room.getCapacity())
                .imageUrl(room.getImageUrl())
                .status(room.getStatus())
                // Set Flatten Data
                .propertyId(room.getProperty().getId())
                .propertyName(propName) // QUAN TRỌNG
                .fullAddress(fullAddr)
                .build();
    }
}