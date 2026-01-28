package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.contract.*;
import com.trandnquang.roomie.entity.*;
import com.trandnquang.roomie.model.enums.ContractStatus;
import com.trandnquang.roomie.model.enums.RoomStatus;
import com.trandnquang.roomie.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;
    private final UtilityRepository utilityRepository;

    @Transactional // QUAN TRỌNG: Đảm bảo tính nhất quán (Atomicity)
    public ContractResponse createContract(CreateContractRequest request) {

        // 1. Kiểm tra Phòng (Room)
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not available for rent");
        }

        // 2. Kiểm tra Khách ký HĐ (Tenant)
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // 3. Tạo Hợp đồng và mã HĐ tự động (HD-yyyyMMdd-4SốCuốiUUID)
        String contractCode = "HD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Contract contract = Contract.builder()
                .contractCode(contractCode)
                .room(room)
                .tenant(tenant)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .rentPrice(request.getRentPrice())
                .depositAmount(request.getDepositAmount())
                .paymentCycle(request.getPaymentCycle())
                .status(ContractStatus.ACTIVE)
                .build();

        // 4. Xử lý Cư dân (Residents) - Manual Mapping
        if (request.getResidents() != null) {
            request.getResidents().forEach(resDTO -> {
                Resident resident = Resident.builder()
                        .fullName(resDTO.getFullName())
                        .phoneNumber(resDTO.getPhoneNumber())
                        .identityCardNumber(resDTO.getIdentityCardNumber())
                        .isContractHolder(resDTO.isContractHolder())
                        .moveInDate(request.getStartDate())
                        .build();
                contract.addResident(resident); // Dùng helper method trong Entity
            });
        }

        // 5. Xử lý Chốt giá Dịch vụ (Locked Service Prices)
        if (request.getUtilities() != null) {
            request.getUtilities().forEach(sReq -> {
                Utility originalUtility = utilityRepository.findById(sReq.getUtilityId())
                        .orElseThrow(() -> new RuntimeException("Utility not found"));

                ContractUtility contractUtility = ContractUtility.builder()
                        .utility(originalUtility)
                        .amount(sReq.getAmount())
                        .startIndex(sReq.getStartIndex())
                        .build();
                contract.addContractUtility(contractUtility);
            });
        }

        // 6. Cập nhật trạng thái Phòng thành OCCUPIED
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // 7. Lưu toàn bộ Hợp đồng (Cascade sẽ tự lưu Resident và ContractService)
        Contract savedContract = contractRepository.save(contract);

        return mapToResponse(savedContract);
    }

    @Transactional
    public void addResidentToContract(Long contractId, ResidentRequest resDTO) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        // Check capacity logic (Optional)
        if (contract.getResidents().size() >= contract.getRoom().getCapacity()) {
            throw new RuntimeException("Room capacity exceeded");
        }

        Resident resident = Resident.builder()
                .fullName(resDTO.getFullName())
                .phoneNumber(resDTO.getPhoneNumber())
                .identityCardNumber(resDTO.getIdentityCardNumber())
                .isContractHolder(false)
                .contract(contract)
                .moveInDate(java.time.LocalDate.now())
                .build();

        contract.addResident(resident);
        contractRepository.save(contract);
    }

    // --- HELPER: MANUAL MAPPING ---
    private ContractResponse mapToResponse(Contract contract) {
        return ContractResponse.builder()
                .id(contract.getId())
                .contractCode(contract.getContractCode())
                .roomNumber(contract.getRoom().getRoomNumber())
                .propertyName(contract.getRoom().getProperty().getName())
                .tenantName(contract.getTenant().getFullName())
                .tenantPhone(contract.getTenant().getPhoneNumber())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .rentPrice(contract.getRentPrice())
                .depositAmount(contract.getDepositAmount())
                .status(contract.getStatus())
                .residents(contract.getResidents().stream().map(r -> ResidentResponse.builder()
                        .id(r.getId())
                        .fullName(r.getFullName())
                        .phoneNumber(r.getPhoneNumber())
                        .identityCardNumber(r.getIdentityCardNumber())
                        .isContractHolder(r.isContractHolder())
                        .build()).collect(Collectors.toList()))
                .utilities(contract.getContractUtilities().stream().map(cs -> ContractUtilityResponse.builder()
                        .id(cs.getId())
                        .utilityId(cs.getUtility().getId())
                        .utilityName(cs.getUtility().getName())
                        .utilityUnit(cs.getUtility().getUnit())
                        .price(cs.getUtility().getBasePrice()) // Đây chính là giá "snapshot"
                        .amount(cs.getAmount())
                        .startIndex(cs.getStartIndex())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}