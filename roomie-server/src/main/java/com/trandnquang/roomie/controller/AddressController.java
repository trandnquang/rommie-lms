package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.model.address.District;
import com.trandnquang.roomie.model.address.Province;
import com.trandnquang.roomie.model.address.Ward;
import com.trandnquang.roomie.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getProvinces() {
        return ResponseEntity.ok(addressService.getAllProvinces());
    }

    @GetMapping("/districts")
    public ResponseEntity<List<District>> getDistricts(@RequestParam String provinceName) {
        return ResponseEntity.ok(addressService.getDistrictsByProvince(provinceName));
    }

    @GetMapping("/wards")
    public ResponseEntity<List<Ward>> getWards(@RequestParam String provinceName, @RequestParam String districtName) {
        return ResponseEntity.ok(addressService.getWardsByDistrict(provinceName, districtName));
    }
}