// PropertyController.java
package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.property.PropertyRequest;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Property> createProperty(@Valid @RequestBody PropertyRequest request) {
        return new ResponseEntity<>(propertyService.createProperty(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    // Thêm các method update/delete tương tự
}