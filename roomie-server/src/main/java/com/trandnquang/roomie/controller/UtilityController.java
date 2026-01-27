package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.utility.UtilityRequest;
import com.trandnquang.roomie.entity.Utility;
import com.trandnquang.roomie.service.UtilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilities")
@RequiredArgsConstructor
public class UtilityController {

    private final UtilityService utilityService;

    @PostMapping
    public ResponseEntity<Utility> create(@Valid @RequestBody UtilityRequest request) {
        return ResponseEntity.ok(utilityService.createUtility(request));
    }

    @GetMapping
    public ResponseEntity<List<Utility>> getAll() {
        return ResponseEntity.ok(utilityService.getAllUtilities());
    }
}