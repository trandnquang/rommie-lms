package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.RoomRequest;
import com.trandnquang.roomie.dto.response.RoomResponse;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.entity.Room;
import com.trandnquang.roomie.repo.PropertyRepository;
import com.trandnquang.roomie.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;

    public List<RoomResponse> getRoomsByProperty(Long propertyId) {
        return roomRepository.findByPropertyId(propertyId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoomResponse createRoom(RoomRequest request) {
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Room room = new Room();
        room.setProperty(property);
        room.setRoomNumber(request.getRoomNumber());
        room.setFloorNumber(request.getFloorNumber());
        room.setArea(request.getArea());
        room.setPrice(request.getPrice());
        room.setCapacity(request.getCapacity());
        room.setImageUrl(request.getImageUrl());
        room.setStatus("AVAILABLE");

        return mapToResponse(roomRepository.save(room));
    }

    private RoomResponse mapToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setPropertyName(room.getProperty().getName()); // Flattening
        response.setRoomNumber(room.getRoomNumber());
        response.setFloorNumber(room.getFloorNumber());
        response.setArea(room.getArea());
        response.setPrice(room.getPrice());
        response.setCapacity(room.getCapacity());
        response.setStatus(room.getStatus());
        response.setImageUrl(room.getImageUrl());
        return response;
    }
}