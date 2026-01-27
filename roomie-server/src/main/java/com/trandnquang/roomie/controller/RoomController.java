package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.room.RoomRequest;
import com.trandnquang.roomie.dto.room.RoomResponse;
import com.trandnquang.roomie.model.enums.RoomStatus;
import com.trandnquang.roomie.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAll(@RequestParam(required = false) Long propertyId) {
        if (propertyId != null) {
            return ResponseEntity.ok(roomService.getRoomsByProperty(propertyId));
        }
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
        roomService.updateRoomStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}