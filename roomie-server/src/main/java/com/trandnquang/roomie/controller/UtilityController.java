package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.utility.UtilityRequest;
import com.trandnquang.roomie.entity.Utility;
import com.trandnquang.roomie.service.UtilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilities")
@RequiredArgsConstructor
public class UtilityController {

    private final UtilityService utilityService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Utility> createUtility(@Valid @RequestBody UtilityRequest request) {
        return new ResponseEntity<>(utilityService.createUtility(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Utility>> getAllUtilities() {
        return ResponseEntity.ok(utilityService.getAllUtilities());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Utility> updateUtility(@PathVariable Long id, @Valid @RequestBody UtilityRequest request) {
        return ResponseEntity.ok(utilityService.updateUtility(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUtility(@PathVariable Long id) {
        utilityService.deleteUtility(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        utilityService.toggleStatus(id);
        return ResponseEntity.ok().build();
    }
}