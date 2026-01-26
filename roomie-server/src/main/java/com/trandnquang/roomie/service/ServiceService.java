package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.ServiceRequest;
import com.trandnquang.roomie.dto.response.ServiceResponse;
import com.trandnquang.roomie.entity.Service; // Entity
import com.trandnquang.roomie.repo.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceResponse createService(ServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName());
        service.setPrice(request.getPrice());
        service.setUnit(request.getUnit());
        return mapToResponse(serviceRepository.save(service));
    }

    private ServiceResponse mapToResponse(Service service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setPrice(service.getPrice());
        response.setUnit(service.getUnit());
        return response;
    }
}