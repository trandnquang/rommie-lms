package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.tenant.TenantRequest;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // Tạo mới khách thuê
    @PostMapping
    public ResponseEntity<Tenant> create(@Valid @RequestBody TenantRequest request) {
        return ResponseEntity.ok(tenantService.createTenant(request));
    }

    // Lấy danh sách khách thuê
    @GetMapping
    public ResponseEntity<List<Tenant>> getAll() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    // Lấy chi tiết 1 khách
    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    // Cập nhật thông tin
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> update(@PathVariable Long id, @Valid @RequestBody TenantRequest request) {
        return ResponseEntity.ok(tenantService.updateTenant(id, request));
    }

    // Xóa khách
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}