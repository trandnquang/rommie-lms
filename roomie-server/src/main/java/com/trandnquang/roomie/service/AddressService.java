package com.trandnquang.roomie.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trandnquang.roomie.model.address.District;
import com.trandnquang.roomie.model.address.Province;
import com.trandnquang.roomie.model.address.Ward;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {

    private final ObjectMapper objectMapper;

    // Cache dữ liệu trong RAM để truy xuất cực nhanh (O(n))
    private List<Province> addressData;

    @PostConstruct
    public void loadAddressData() {
        try {
            // Đảm bảo file json nằm ở: src/main/resources/data/address_master.json
            ClassPathResource resource = new ClassPathResource("data/address_master.json");

            if (!resource.exists()) {
                log.warn("File address_master.json not found. Address validation disabled.");
                addressData = Collections.emptyList();
                return;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                // Jackson sẽ tự động map unit_type -> unitType nhờ @JsonProperty
                addressData = objectMapper.readValue(inputStream, new TypeReference<List<Province>>() {});
                log.info("Loaded {} provinces from JSON file.", addressData.size());
            }
        } catch (Exception e) {
            log.error("Failed to load address data", e);
            addressData = Collections.emptyList();
        }
    }

    // --- READ OPERATIONS (Cho Frontend gọi để đổ vào Dropdown) ---

    public List<Province> getAllProvinces() {
        return addressData;
    }

    public List<District> getDistrictsByProvince(String provinceName) {
        if (provinceName == null) return Collections.emptyList();

        return addressData.stream()
                .filter(p -> p.getName().equalsIgnoreCase(provinceName.trim()))
                .findFirst()
                .map(Province::getDistricts)
                .orElse(Collections.emptyList());
    }

    public List<Ward> getWardsByDistrict(String provinceName, String districtName) {
        if (provinceName == null || districtName == null) return Collections.emptyList();

        // 1. Tìm Tỉnh
        Optional<Province> provinceOpt = addressData.stream()
                .filter(p -> p.getName().equalsIgnoreCase(provinceName.trim()))
                .findFirst();

        if (provinceOpt.isEmpty()) return Collections.emptyList();

        // 2. Tìm Huyện trong Tỉnh đó
        return provinceOpt.get().getDistricts().stream()
                .filter(d -> d.getName().equalsIgnoreCase(districtName.trim()))
                .findFirst()
                .map(District::getWards)
                .orElse(Collections.emptyList());
    }

    // --- VALIDATION LOGIC (Dùng cho Tenant/Contract Service) ---

    public boolean isValidAddress(String city, String district, String ward) {
        if (city == null || district == null || ward == null) return false;

        String safeCity = city.trim();
        String safeDistrict = district.trim();
        String safeWard = ward.trim();

        // 1. Validate Tỉnh/Thành
        Optional<Province> p = addressData.stream()
                .filter(province -> province.getName().equalsIgnoreCase(safeCity))
                .findFirst();
        if (p.isEmpty()) return false;

        // 2. Validate Quận/Huyện (Phải thuộc Tỉnh trên)
        if (p.get().getDistricts() == null) return false;

        Optional<District> d = p.get().getDistricts().stream()
                .filter(dist -> dist.getName().equalsIgnoreCase(safeDistrict))
                .findFirst();
        if (d.isEmpty()) return false;

        // 3. Validate Phường/Xã (Phải thuộc Quận trên)
        if (d.get().getWards() == null) return false;

        return d.get().getWards().stream()
                .anyMatch(w -> w.getName().equalsIgnoreCase(safeWard));
    }
}