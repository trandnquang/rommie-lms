package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.tenant.TenantRequest;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.repo.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final AddressService addressService; // Inject để validate địa chỉ

    // --- 1. CREATE TENANT ---
    @Transactional
    public Tenant createTenant(TenantRequest request) {
        // Validation 1: Kiểm tra trùng CMND/CCCD
        if (tenantRepository.findByIdentityCardNumber(request.getIdentityCardNumber()).isPresent()) {
            throw new RuntimeException("Identity Card Number already exists: " + request.getIdentityCardNumber());
        }

        // Validation 2: Kiểm tra Địa chỉ (Tỉnh - Huyện - Xã) có khớp nhau không
        if (!addressService.isValidAddress(request.getCity(), request.getDistrict(), request.getWard())) {
            throw new IllegalArgumentException("Địa chỉ không hợp lệ. Vui lòng kiểm tra lại Tỉnh/Huyện/Xã.");
        }

        // Manual Mapping: DTO -> Entity
        Tenant tenant = Tenant.builder()
                .fullName(request.getFullName())
                .identityCardNumber(request.getIdentityCardNumber())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .addressLine(request.getAddressLine())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .isDeleted(false)
                .build();

        return tenantRepository.save(tenant);
    }

    // --- 2. GET ALL ---
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    // --- 3. GET BY ID ---
    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
    }

    // --- 4. UPDATE ---
    @Transactional
    public Tenant updateTenant(Long id, TenantRequest request) {
        Tenant tenant = getTenantById(id);

        // Nếu sửa địa chỉ, phải validate lại
        if (!addressService.isValidAddress(request.getCity(), request.getDistrict(), request.getWard())) {
            throw new IllegalArgumentException("Địa chỉ cập nhật không hợp lệ.");
        }

        // Cập nhật thông tin
        tenant.setFullName(request.getFullName());
        tenant.setPhoneNumber(request.getPhoneNumber());
        tenant.setEmail(request.getEmail());
        tenant.setBirthday(request.getBirthday());
        tenant.setGender(request.getGender());
        tenant.setAddressLine(request.getAddressLine());
        tenant.setWard(request.getWard());
        tenant.setDistrict(request.getDistrict());
        tenant.setCity(request.getCity());
        // Lưu ý: Thường không cho sửa số CMND tùy tiện, nên tôi không set lại field đó ở đây

        return tenantRepository.save(tenant);
    }

    // --- 5. DELETE ---
    @Transactional
    public void deleteTenant(Long id) {
        Tenant tenant = getTenantById(id);
        // Soft Delete (nhờ @SQLDelete trong Entity)
        tenantRepository.delete(tenant);
    }
}