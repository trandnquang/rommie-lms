package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.tenant.TenantRequest;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    /**
     * TẠO HỒ SƠ KHÁCH THUÊ
     * (Thường dùng khi khách đến xem phòng và muốn giữ chỗ trước khi ký HĐ)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody TenantRequest request) {
        return new ResponseEntity<>(tenantService.createTenant(request), HttpStatus.CREATED);
    }

    /**
     * LẤY DANH SÁCH KHÁCH
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Tenant>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    /**
     * XEM CHI TIẾT
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    /**
     * CẬP NHẬT THÔNG TIN
     * (Ví dụ: Khách đổi số điện thoại, sửa sai chính tả tên)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @Valid @RequestBody TenantRequest request) {
        return ResponseEntity.ok(tenantService.updateTenant(id, request));
    }

    /**
     * XÓA HỒ SƠ
     * (Chỉ Admin được xóa. Lưu ý: Chỉ xóa được nếu khách chưa dính vào Hợp đồng nào,
     * hoặc dùng Soft Delete như chúng ta đã thiết kế)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}