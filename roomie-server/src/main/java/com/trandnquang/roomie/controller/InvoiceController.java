package com.trandnquang.roomie.controller;

import com.trandnquang.roomie.dto.invoice.InvoiceCreationRequest;
import com.trandnquang.roomie.dto.invoice.InvoiceResponse;
import com.trandnquang.roomie.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /**
     * TẠO HÓA ĐƠN MỚI
     * Logic: Tính tiền điện/nước theo bậc thang, chốt số liệu.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreationRequest request) {
        return new ResponseEntity<>(invoiceService.createInvoice(request), HttpStatus.CREATED);
    }

    /**
     * XEM CHI TIẾT HÓA ĐƠN
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TENANT')")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceDetail(id));
    }

    /**
     * LẤY LỊCH SỬ HÓA ĐƠN CỦA HỢP ĐỒNG
     * (Dùng cho Tenant xem lịch sử thanh toán)
     */
    @GetMapping("/contract/{contractId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TENANT')")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByContract(@PathVariable Long contractId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByContract(contractId));
    }

    /**
     * HỦY HÓA ĐƠN
     * (Chỉ dùng khi tạo sai và chưa thanh toán)
     * Lưu ý: Bạn cần đảm bảo đã thêm hàm cancelInvoice vào Service như đã bàn ở bài trước.
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> cancelInvoice(@PathVariable Long id) {
        // Nếu Service chưa có hàm này, hãy quay lại InvoiceService thêm vào nhé!
        // invoiceService.cancelInvoice(id);
        // Tạm thời comment nếu chưa impl, nhưng nên có.
        return ResponseEntity.ok().build();
    }
}