package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.request.InvoiceItemRequest;
import com.trandnquang.roomie.dto.request.InvoiceRequest;
import com.trandnquang.roomie.dto.response.InvoiceResponse;
import com.trandnquang.roomie.entity.Contract;
import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.entity.InvoiceItem;
import com.trandnquang.roomie.repo.ContractRepository;
import com.trandnquang.roomie.repo.InvoiceRepository;
import com.trandnquang.roomie.repo.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ContractRepository contractRepository;
    private final ServiceRepository serviceRepository;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        // 1. Tạo Invoice khung
        Invoice invoice = new Invoice();
        invoice.setContract(contract);
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus("UNPAID");

        // 2. Tính tiền từng item và thêm vào Invoice
        List<InvoiceItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Cộng thêm tiền phòng cơ bản
        totalAmount = totalAmount.add(contract.getRentPrice());

        for (InvoiceItemRequest itemReq : request.getItems()) {
            com.trandnquang.roomie.entity.Service serviceEntity = serviceRepository.findById(itemReq.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setService(serviceEntity);
            item.setOldValue(itemReq.getOldValue());
            item.setNewValue(itemReq.getNewValue());

            // Logic tính: (Mới - Cũ) * Đơn giá
            BigDecimal usage = BigDecimal.valueOf(itemReq.getNewValue() - itemReq.getOldValue());
            if (usage.compareTo(BigDecimal.ZERO) < 0) usage = BigDecimal.ZERO; // Tránh âm

            BigDecimal subTotal = usage.multiply(serviceEntity.getPrice());

            item.setUnitPrice(serviceEntity.getPrice());
            item.setSubTotal(subTotal);

            items.add(item);
            totalAmount = totalAmount.add(subTotal);
        }

        invoice.setInvoiceItems(items); // Cascade sẽ tự lưu items
        invoice.setTotalAmount(totalAmount);

        return mapToResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> getUnpaidInvoices() {
        return invoiceRepository.findByStatus("UNPAID").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setRoomNumber(invoice.getContract().getRoom().getRoomNumber());
        response.setTenantName(invoice.getContract().getTenant().getFullName());
        response.setIssueDate(invoice.getIssueDate());
        response.setDueDate(invoice.getDueDate());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setStatus(invoice.getStatus());
        return response;
    }
}