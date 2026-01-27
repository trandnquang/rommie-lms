package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.property.PropertyRequest;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> create(@Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.createProperty(request));
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAll() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.updateProperty(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}