package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.ContractRequest;
import com.trandnquang.roomie.dto.response.ContractResponse;
import com.trandnquang.roomie.entity.Contract;
import com.trandnquang.roomie.entity.Room;
import com.trandnquang.roomie.entity.Tenant;
import com.trandnquang.roomie.repo.ContractRepository;
import com.trandnquang.roomie.repo.RoomRepository;
import com.trandnquang.roomie.repo.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public ContractResponse createContract(ContractRequest request) {
        // 1. Validate Room & Tenant
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        if (contractRepository.existsByRoomIdAndStatus(room.getId(), "ACTIVE")) {
            throw new RuntimeException("Room is already rented");
        }

        // 2. Create Contract
        Contract contract = new Contract();
        contract.setRoom(room);
        contract.setTenant(tenant);
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setDeposit(request.getDeposit());
        contract.setRentPrice(request.getRentPrice());
        contract.setOccupantCount(request.getOccupantCount());
        contract.setStatus("ACTIVE");

        Contract saved = contractRepository.save(contract);

        // 3. Update Room Status
        room.setStatus("OCCUPIED");
        roomRepository.save(room);

        return mapToResponse(saved);
    }

    public List<ContractResponse> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Method Terminate Contract... (Giữ nguyên logic cũ nhưng ko cần DTO input)

    private ContractResponse mapToResponse(Contract contract) {
        ContractResponse response = new ContractResponse();
        response.setId(contract.getId());
        response.setRoomNumber(contract.getRoom().getRoomNumber());
        response.setPropertyName(contract.getRoom().getProperty().getName());
        response.setTenantName(contract.getTenant().getFullName());
        response.setTenantPhone(contract.getTenant().getPhoneNumber());
        response.setStartDate(contract.getStartDate());
        response.setEndDate(contract.getEndDate());
        response.setDeposit(contract.getDeposit());
        response.setRentPrice(contract.getRentPrice());
        response.setStatus(contract.getStatus());
        return response;
    }
}