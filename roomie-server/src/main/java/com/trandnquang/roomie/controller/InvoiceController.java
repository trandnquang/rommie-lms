package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.invoice.InvoiceCreationRequest;
import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/calculate") // Endpoint tạo và tính toán hóa đơn
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody InvoiceCreationRequest request) {
        return ResponseEntity.ok(invoiceService.createInvoice(request));
    }
}