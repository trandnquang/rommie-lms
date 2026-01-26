package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.TenantRequest;
import com.trandnquang.roomie.dto.response.TenantResponse;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.repo.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TenantResponse createTenant(TenantRequest request) {
        Optional<Tenant> existing = tenantRepository.findByIdentityNumber(request.getIdentityNumber());
        if (existing.isPresent()) {
            throw new RuntimeException("Tenant already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setIdentityNumber(request.getIdentityNumber());
        tenant.setFullName(request.getFullName());
        tenant.setPhoneNumber(request.getPhoneNumber());
        tenant.setEmail(request.getEmail());
        tenant.setAddressLine(request.getAddressLine());
        tenant.setWard(request.getWard());
        tenant.setDistrict(request.getDistrict());
        tenant.setCity(request.getCity());
        tenant.setGender(request.getGender());
        tenant.setDateOfBirth(request.getDateOfBirth());

        return mapToResponse(tenantRepository.save(tenant));
    }

    private TenantResponse mapToResponse(Tenant tenant) {
        TenantResponse response = new TenantResponse();
        response.setId(tenant.getId());
        response.setIdentityNumber(tenant.getIdentityNumber());
        response.setFullName(tenant.getFullName());
        response.setPhoneNumber(tenant.getPhoneNumber());
        response.setEmail(tenant.getEmail());
        // Gộp địa chỉ hiển thị cho đẹp
        String fullAddress = String.format("%s, %s, %s, %s",
             tenant.getAddressLine(), tenant.getWard(), tenant.getDistrict(), tenant.getCity());
        response.setAddressFull(fullAddress);
        response.setGender(tenant.getGender());
        response.setDateOfBirth(tenant.getDateOfBirth());
        return response;
    }
}