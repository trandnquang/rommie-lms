package com.trandnquang.roomie.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trandnquang.roomie.model.address.District;
import com.trandnquang.roomie.model.address.Province;
import com.trandnquang.roomie.model.address.Ward;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private List<Province> addressData; // Cache dữ liệu trong RAM

    // 1. Tự động chạy khi khởi động Server để load JSON vào RAM
    @PostConstruct
    public void loadAddressData() {
        try {
            // Đảm bảo file json nằm ở: src/main/resources/data/address_master.json
            InputStream inputStream = getClass().getResourceAsStream("/data/address_master.json");
            if (inputStream == null) {
                throw new RuntimeException("File address_master.json not found in resources/data/");
            }
            addressData = objectMapper.readValue(inputStream, new TypeReference<List<Province>>() {});
            log.info("Loaded {} provinces from JSON file.", addressData.size());
        } catch (Exception e) {
            log.error("Failed to load address data", e);
            addressData = Collections.emptyList();
        }
    }

    // 2. Các hàm Helper để Controller gọi (đổ dữ liệu ra Frontend)
    public List<Province> getAllProvinces() {
        return addressData;
    }

    public List<District> getDistrictsByProvince(String provinceName) {
        if (provinceName == null) return Collections.emptyList();
        return addressData.stream()
                .filter(p -> p.getName().equalsIgnoreCase(provinceName))
                .findFirst()
                .map(Province::getDistricts)
                .orElse(Collections.emptyList());
    }

    public List<Ward> getWardsByDistrict(String provinceName, String districtName) {
        if (districtName == null) return Collections.emptyList();
        return getDistrictsByProvince(provinceName).stream()
                .filter(d -> d.getName().equalsIgnoreCase(districtName))
                .findFirst()
                .map(District::getWards)
                .orElse(Collections.emptyList());
    }

    // 3. CORE LOGIC: Kiểm tra tính hợp lệ (Validation)
    // Hàm này sẽ được PropertyService và TenantService gọi
    public boolean isValidAddress(String city, String district, String ward) {
        if (city == null || district == null || ward == null) return false;

        // 1. Tìm Tỉnh/Thành
        Optional<Province> p = addressData.stream()
                .filter(province -> province.getName().equalsIgnoreCase(city))
                .findFirst();
        if (p.isEmpty()) return false;

        // 2. Tìm Quận/Huyện trong Tỉnh đó
        Optional<District> d = p.get().getDistricts().stream()
                .filter(dist -> dist.getName().equalsIgnoreCase(district))
                .findFirst();
        if (d.isEmpty()) return false;

        // 3. Tìm Phường/Xã trong Quận đó
        boolean wardExists = d.get().getWards().stream()
                .anyMatch(w -> w.getName().equalsIgnoreCase(ward));

        return wardExists;
    }
}