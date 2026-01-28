package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.contract.*;
import com.trandnquang.roomie.dto.resident.ResidentRequest;
import com.trandnquang.roomie.dto.resident.ResidentResponse;
import com.trandnquang.roomie.entity.*;
import com.trandnquang.roomie.model.enums.ContractStatus;
import com.trandnquang.roomie.model.enums.RoomStatus;
import com.trandnquang.roomie.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;
    private final UtilityRepository utilityRepository;
    private final TenantService tenantService; // Reuse logic tạo Tenant

    @Transactional
    public ContractResponse createContract(CreateContractRequest request) {
        log.info("Creating contract for Room ID: {}", request.getRoomId());

        // 1. Kiểm tra Phòng
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room is not AVAILABLE. Current status: " + room.getStatus());
        }

        // 2. Xử lý Tenant (Logic: Dùng cũ HOẶC Tạo mới)
        Tenant tenant;
        if (request.getNewTenant() != null) {
            // Gọi TenantService để reuse logic validate địa chỉ & CMND
            tenant = tenantService.createTenant(request.getNewTenant());
        } else if (request.getTenantId() != null) {
            tenant = tenantRepository.findById(request.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));
        } else {
            throw new IllegalArgumentException("Vui lòng chọn Khách cũ hoặc nhập thông tin Khách mới.");
        }

        // 3. Tạo Mã HĐ & Entity
        String contractCode = "HD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Contract contract = Contract.builder()
                .contractCode(contractCode)
                .room(room)
                .tenant(tenant)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .rentPrice(request.getRentPrice()) // Giá chốt phòng
                .depositAmount(request.getDepositAmount())
                .paymentCycle(request.getPaymentCycle())
                .status(ContractStatus.ACTIVE)
                .build();

        // 4. Xử lý Dịch vụ & CHỐT GIÁ (PRICE LOCKING)
        if (request.getUtilities() != null) {
            List<ContractUtility> contractUtilities = new ArrayList<>();
            for (ContractUtilityRequest utilReq : request.getUtilities()) {
                Utility originalUtility = utilityRepository.findById(utilReq.getUtilityId())
                        .orElseThrow(() -> new RuntimeException("Utility not found ID: " + utilReq.getUtilityId()));

                ContractUtility cu = ContractUtility.builder()
                        .contract(contract)
                        .utility(originalUtility)
                        .amount(utilReq.getAmount())
                        .startIndex(utilReq.getStartIndex() != null ? utilReq.getStartIndex() : BigDecimal.ZERO)
                        // --- SNAPSHOT GIÁ (QUAN TRỌNG NHẤT) ---
                        // Lưu cứng giá tại thời điểm ký để sau này Utility tăng giá không ảnh hưởng HĐ cũ
                        .lockedPrice(originalUtility.getBasePrice())
                        .lockedTierConfig(originalUtility.getTierConfig()) // Copy JSONB
                        // ---------------------------------------
                        .build();
                contractUtilities.add(cu);
            }
            contract.setContractUtilities(contractUtilities);
        }

        // 5. Xử lý Cư dân (Residents)
        if (request.getResidents() != null) {
            List<Resident> residents = request.getResidents().stream().map(resReq -> Resident.builder()
                    .contract(contract)
                    .fullName(resReq.getFullName())
                    .phoneNumber(resReq.getPhoneNumber())
                    .identityCardNumber(resReq.getIdentityCardNumber())
                    .isContractHolder(resReq.isContractHolder())
                    .moveInDate(request.getStartDate())
                    .build()).collect(Collectors.toList());
            contract.setResidents(residents);
        }

        // 6. Cập nhật trạng thái phòng -> OCCUPIED
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // 7. Lưu Hợp đồng (Cascade All sẽ lưu Utility & Resident)
        Contract savedContract = contractRepository.save(contract);

        return mapToResponse(savedContract);
    }

    @Transactional
    public void addResidentToContract(Long contractId, ResidentRequest resDTO) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new RuntimeException("Cannot add resident to non-active contract");
        }

        // Check Capacity
        int currentCount = contract.getResidents().size();
        if (currentCount >= contract.getRoom().getCapacity()) {
            throw new RuntimeException("Room capacity exceeded (" + currentCount + "/" + contract.getRoom().getCapacity() + ")");
        }

        Resident resident = Resident.builder()
                .contract(contract)
                .fullName(resDTO.getFullName())
                .phoneNumber(resDTO.getPhoneNumber())
                .identityCardNumber(resDTO.getIdentityCardNumber())
                .isContractHolder(false) // Thêm sau thì ko thể là chủ HĐ
                .moveInDate(LocalDate.now())
                .build();

        // Không cần save riêng, add vào list rồi save contract cũng được, hoặc dùng repo riêng
        // Ở đây dùng cách add vào collection của Hibernate
        contract.addResident(resident);
        contractRepository.save(contract);
    }

    public ContractResponse getContractDetail(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        return mapToResponse(contract);
    }

    @Transactional
    public void terminateContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
             throw new RuntimeException("Contract is already ended or cancelled");
        }

        // 1. End Contract
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setActualEndDate(LocalDate.now());

        // 2. Release Room
        contract.getRoom().setStatus(RoomStatus.AVAILABLE);

        // 3. Update Residents move-out
        contract.getResidents().forEach(r -> r.setMoveOutDate(LocalDate.now()));

        contractRepository.save(contract);
    }

    // --- HELPER: MAPPER ---
    private ContractResponse mapToResponse(Contract contract) {
        // Flatten Utilities: Lấy giá ĐÃ CHỐT trong ContractUtility, KHÔNG lấy giá hiện tại
        List<ContractUtilityResponse> utils = contract.getContractUtilities().stream().map(cu -> ContractUtilityResponse.builder()
                .id(cu.getId())
                .utilityId(cu.getUtility().getId())
                .utilityName(cu.getUtility().getName())
                .utilityUnit(cu.getUtility().getUnit())
                .price(cu.getLockedPrice())       // <--- CHUẨN: Giá chốt
                .tierConfig(cu.getLockedTierConfig()) // <--- CHUẨN: Bậc thang chốt
                .amount(cu.getAmount())
                .startIndex(cu.getStartIndex())
                .build()).collect(Collectors.toList());

        List<ResidentResponse> residents = contract.getResidents().stream().map(r -> ResidentResponse.builder()
                .id(r.getId())
                .fullName(r.getFullName())
                .phoneNumber(r.getPhoneNumber())
                .identityCardNumber(r.getIdentityCardNumber())
                .isContractHolder(r.isContractHolder())
                .moveInDate(r.getMoveInDate())
                .build()).collect(Collectors.toList());

        return ContractResponse.builder()
                .id(contract.getId())
                .contractCode(contract.getContractCode())
                .roomId(contract.getRoom().getId())
                .roomNumber(contract.getRoom().getRoomNumber())
                .propertyName(contract.getRoom().getProperty().getName())
                .address(contract.getRoom().getProperty().getAddressLine() + ", " + contract.getRoom().getProperty().getDistrict())
                .tenantId(contract.getTenant().getId())
                .tenantName(contract.getTenant().getFullName())
                .tenantPhone(contract.getTenant().getPhoneNumber())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .rentPrice(contract.getRentPrice())
                .depositAmount(contract.getDepositAmount())
                .paymentCycle(contract.getPaymentCycle())
                .status(contract.getStatus())
                .utilities(utils)
                .residents(residents)
                .build();
    }
}