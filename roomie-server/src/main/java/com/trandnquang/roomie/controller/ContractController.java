package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.contract.ContractResponse;
import com.trandnquang.roomie.dto.contract.CreateContractRequest;
import com.trandnquang.roomie.dto.contract.ResidentRequest;
import com.trandnquang.roomie.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponse> create(@Valid @RequestBody CreateContractRequest request) {
        return ResponseEntity.ok(contractService.createContract(request));
    }

    @PostMapping("/{id}/residents")
    public ResponseEntity<String> addResident(@PathVariable Long id, @Valid @RequestBody ResidentRequest request) {
        contractService.addResidentToContract(id, request);
        return ResponseEntity.ok("Resident added successfully");
    }
}