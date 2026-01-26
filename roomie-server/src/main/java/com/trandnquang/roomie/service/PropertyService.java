package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.PropertyRequest;
import com.trandnquang.roomie.dto.response.PropertyResponse;
import com.trandnquang.roomie.entity.Property;
import com.trandnquang.roomie.repo.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PropertyResponse createProperty(PropertyRequest request) {
        Property property = new Property();
        property.setName(request.getName());
        property.setAddressLine(request.getAddressLine());
        property.setWard(request.getWard());
        property.setDistrict(request.getDistrict());
        property.setCity(request.getCity());
        property.setDescription(request.getDescription());

        Property saved = propertyRepository.save(property);
        return mapToResponse(saved);
    }

    // Helper: Map Entity -> Response
    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());
        response.setName(property.getName());
        response.setAddressLine(property.getAddressLine());
        response.setWard(property.getWard());
        response.setDistrict(property.getDistrict());
        response.setCity(property.getCity());
        response.setDescription(property.getDescription());
        response.setCreatedAt(property.getCreatedAt());
        return response;
    }
}