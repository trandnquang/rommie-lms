package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.model.address.District;
import com.trandnquang.roomie.model.address.Province;
import com.trandnquang.roomie.model.address.Ward;
import com.trandnquang.roomie.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Lấy danh sách Tỉnh/Thành phố
    @GetMapping("/provinces")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Province>> getProvinces() {
        return ResponseEntity.ok(addressService.getAllProvinces());
    }

    // Lấy Quận/Huyện theo Tên Tỉnh
    @GetMapping("/districts")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<District>> getDistricts(@RequestParam String provinceName) {
        return ResponseEntity.ok(addressService.getDistrictsByProvince(provinceName));
    }

    // Lấy Phường/Xã theo Tên Tỉnh và Tên Quận
    @GetMapping("/wards")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Ward>> getWards(
            @RequestParam String provinceName,
            @RequestParam String districtName) {
        return ResponseEntity.ok(addressService.getWardsByDistrict(provinceName, districtName));
    }
}