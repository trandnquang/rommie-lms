package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.property.PropertyRequest;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.repo.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final AddressService addressService; // Inject service vừa viết ở trên

    // --- 1. CREATE ---
    @Transactional
    public Property createProperty(PropertyRequest request) {
        // VALIDATION ĐỊA CHỈ: Kiểm tra xem Tỉnh - Huyện - Xã có khớp nhau không
        if (!addressService.isValidAddress(request.getCity(), request.getDistrict(), request.getWard())) {
            throw new IllegalArgumentException("Địa chỉ không hợp lệ (Tỉnh/Huyện/Xã không khớp).");
        }

        Property property = Property.builder()
                .name(request.getName())
                .description(request.getDescription())
                .addressLine(request.getAddressLine())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .isDeleted(false)
                .build();

        return propertyRepository.save(property);
    }

    // --- 2. UPDATE ---
    @Transactional
    public Property updateProperty(Long id, PropertyRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));

        // VALIDATION ĐỊA CHỈ KHI SỬA
        if (!addressService.isValidAddress(request.getCity(), request.getDistrict(), request.getWard())) {
            throw new IllegalArgumentException("Địa chỉ cập nhật không hợp lệ.");
        }

        property.setName(request.getName());
        property.setDescription(request.getDescription());
        property.setAddressLine(request.getAddressLine());
        // Cập nhật địa chỉ mới
        property.setWard(request.getWard());
        property.setDistrict(request.getDistrict());
        property.setCity(request.getCity());

        return propertyRepository.save(property);
    }

    // ... (Các hàm getAll, delete giữ nguyên như cũ)
     public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Transactional
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        propertyRepository.delete(property);
    }
}