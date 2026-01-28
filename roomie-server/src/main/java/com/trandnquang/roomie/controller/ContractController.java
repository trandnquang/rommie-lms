package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.contract.ContractResponse;
import com.trandnquang.roomie.dto.contract.CreateContractRequest;
import com.trandnquang.roomie.dto.resident.ResidentRequest;
import com.trandnquang.roomie.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * TẠO HỢP ĐỒNG MỚI (CHECK-IN)
     * API này sẽ:
     * 1. Validate Phòng trống.
     * 2. Tạo Tenant mới (nếu gửi kèm thông tin) hoặc dùng Tenant cũ.
     * 3. Chốt giá dịch vụ (Price Locking).
     * 4. Chuyển trạng thái phòng sang OCCUPIED.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ContractResponse> createContract(@Valid @RequestBody CreateContractRequest request) {
        ContractResponse response = contractService.createContract(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * XEM CHI TIẾT HỢP ĐỒNG
     * Tenant cũng được xem hợp đồng của chính mình (Logic check quyền sở hữu
     * thường nằm ở Service hoặc Custom Security Expression, ở đây tạm để role TENANT).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TENANT')")
    public ResponseEntity<ContractResponse> getContractDetail(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractDetail(id));
    }

    /**
     * THÊM NGƯỜI Ở GHÉP (RESIDENT)
     * Dùng khi có bạn đến ở thêm sau khi đã ký hợp đồng.
     */
    @PostMapping("/{id}/residents")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> addResident(@PathVariable Long id, @Valid @RequestBody ResidentRequest request) {
        contractService.addResidentToContract(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * THANH LÝ HỢP ĐỒNG (CHECK-OUT)
     * Kết thúc hợp đồng, trả phòng về trạng thái AVAILABLE.
     */
    @PostMapping("/{id}/terminate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> terminateContract(@PathVariable Long id) {
        contractService.terminateContract(id);
        return ResponseEntity.ok().build(); // Trả về 200 OK
    }
}