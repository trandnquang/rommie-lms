package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.ResidentRequest;
import com.trandnquang.roomie.dto.response.ResidentResponse;
import com.trandnquang.roomie.entity.Contract;
import com.trandnquang.roomie.entity.Resident;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.repo.ContractRepository;
import com.trandnquang.roomie.repo.ResidentRepository;
import com.trandnquang.roomie.repo.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final ContractRepository contractRepository;
    private final TenantRepository tenantRepository;

    public ResidentResponse addResident(ResidentRequest request) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Resident resident = new Resident();
        resident.setContract(contract);
        resident.setTenant(tenant);
        resident.setRole(request.getRole());
        resident.setMoveInDate(request.getMoveInDate());

        return mapToResponse(residentRepository.save(resident));
    }

    public List<ResidentResponse> getResidentsByContract(Long contractId) {
        return residentRepository.findByContractId(contractId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ResidentResponse mapToResponse(Resident resident) {
        ResidentResponse response = new ResidentResponse();
        response.setId(resident.getId());
        response.setTenantName(resident.getTenant().getFullName());
        response.setTenantPhone(resident.getTenant().getPhoneNumber());
        response.setRole(resident.getRole());
        response.setMoveInDate(resident.getMoveInDate());
        return response;
    }
}