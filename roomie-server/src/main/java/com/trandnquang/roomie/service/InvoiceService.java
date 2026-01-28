package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.invoice.InvoiceCreationRequest;
import com.trandnquang.roomie.entity.*;
import com.trandnquang.roomie.model.enums.InvoiceStatus;
import com.trandnquang.roomie.repo.*;
import com.trandnquang.roomie.util.CalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trandnquang.roomie.model.enums.PricingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public Invoice createInvoice(InvoiceCreationRequest request) {
        // 1. Validate: Tháng này đã chốt chưa?
        if (invoiceRepository.existsByContractIdAndMonthAndYear(request.getContractId(), request.getMonth(), request.getYear())) {
            throw new RuntimeException("Invoice already exists for this month");
        }

        // 2. Lấy thông tin Hợp đồng
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        // 3. Khởi tạo Hóa đơn (Header)
        Invoice invoice = Invoice.builder()
                .contract(contract)
                .invoiceCode("INV-" + request.getYear() + request.getMonth() + "-" + contract.getContractCode())
                .month(request.getMonth())
                .year(request.getYear())
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(5)) // Hạn đóng tiền 5 ngày
                .status(InvoiceStatus.DRAFT) // Mới tạo thì để DRAFT
                .paidAmount(BigDecimal.ZERO)
                .isDeleted(false)
                .build();

        // Map request readings để dễ tìm kiếm (UtilityId -> NewIndex)
        Map<Long, BigDecimal> readingsMap = request.getReadings() != null ?
                request.getReadings().stream().collect(Collectors.toMap(
                        InvoiceCreationRequest.UtilityReading::getUtilityId,
                        InvoiceCreationRequest.UtilityReading::getNewIndex
                )) : Map.of();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceDetail> details = new ArrayList<>();

        // 4. TÍNH TIỀN DỊCH VỤ (UTILITIES)
        for (ContractUtility cu : contract.getContractUtilities()) {
            Utility utility = cu.getUtility();
            BigDecimal amount = BigDecimal.valueOf(cu.getAmount());
            BigDecimal finalPrice = BigDecimal.ZERO;
            BigDecimal usage = BigDecimal.ZERO;
            BigDecimal oldIndex = BigDecimal.ZERO;
            BigDecimal newIndex = BigDecimal.ZERO;
            String description = "";

            // LOGIC TÍNH TOÁN THEO LOẠI GIÁ
            switch (utility.getPricingType()) {
				case PricingType.TIERED: // Bậc thang (Điện)
				case PricingType.FLAT:   // Đồng giá theo chỉ số (Nước m3)
                    // Cần chỉ số cũ và mới
                    oldIndex = getOldIndex(contract, utility.getId(), request.getMonth(), request.getYear());
                    newIndex = readingsMap.getOrDefault(utility.getId(), oldIndex); // Nếu ko gửi lên thì coi như ko dùng (new=old)

                    if (newIndex.compareTo(oldIndex) < 0) {
                        throw new RuntimeException("New index cannot be smaller than old index for utility: " + utility.getName());
                    }

                    usage = newIndex.subtract(oldIndex);

                    if (PricingType.TIERED.equals(utility.getPricingType())) {
                        finalPrice = CalculatorUtils.calculateTieredCost(usage, utility.getTierConfig());
                        description = String.format("Bậc thang: %s - %s (SD: %s)", oldIndex, newIndex, usage);
                    } else {
                        // FLAT (VD: Nước 25k/m3) -> Giá * Số khối
                        finalPrice = CalculatorUtils.mul(usage, utility.getBasePrice());
                        description = String.format("Theo chỉ số: %s - %s (SD: %s)", oldIndex, newIndex, usage);
                    }
                    break;

				case PricingType.FIXED: // Cố định (Internet)
                    // Giá * Số lượng (thường là 1)
                    finalPrice = CalculatorUtils.mul(utility.getBasePrice(), amount);
                    usage = amount;
                    description = "Phí cố định hàng tháng";
                    break;

                default: // FLAT nhưng ko theo chỉ số (VD: Rác theo đầu người)
                     finalPrice = CalculatorUtils.mul(utility.getBasePrice(), amount);
                     usage = amount;
                     description = "Tính theo số lượng: " + amount;
                     break;
            }

            // Tạo chi tiết hóa đơn cho dịch vụ này
            InvoiceDetail detail = InvoiceDetail.builder()
                    .invoice(invoice)
                    .utility(utility)
                    .utilityName(utility.getName())
                    .oldIndex(oldIndex)
                    .newIndex(newIndex)
                    //.usageAmount(usage) // Cột này là Generated Column trong DB, ko cần set
                    .appliedPrice(utility.getBasePrice()) // Snapshot giá gốc
                    .totalPrice(finalPrice)
                    .description(description)
                    .build();

            details.add(detail);
            totalAmount = totalAmount.add(finalPrice);
        }

        // 5. TÍNH TIỀN PHÒNG (RENT)
        BigDecimal rentPrice = contract.getRentPrice();
        InvoiceDetail rentDetail = InvoiceDetail.builder()
                .invoice(invoice)
                .utility(null) // Tiền phòng ko phải utility
                .utilityName("Tiền thuê phòng tháng " + request.getMonth())
                .totalPrice(rentPrice)
                .description("Giá thuê cố định theo hợp đồng")
                .build();
        details.add(rentDetail);
        totalAmount = totalAmount.add(rentPrice);

        // 6. Lưu xuống DB
        invoice.setTotalAmount(totalAmount);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        details.forEach(d -> d.setInvoice(savedInvoice)); // Gán ID cha vào con
        invoiceDetailRepository.saveAll(details);

        return savedInvoice;
    }

    // --- PRIVATE HELPER: Lấy chỉ số cũ ---
    private BigDecimal getOldIndex(Contract contract, Long utilityId, int currentMonth, int currentYear) {
        // Cách 1: Tìm hóa đơn tháng gần nhất trước đó
        Optional<Invoice> lastInvoice = invoiceRepository.findLatestInvoiceBefore(contract.getId(), currentMonth, currentYear);

        if (lastInvoice.isPresent()) {
            // Tìm chi tiết dịch vụ đó trong hóa đơn cũ để lấy New Index -> Làm Old Index cho tháng này
            Optional<InvoiceDetail> detail = invoiceDetailRepository.findByInvoiceIdAndUtilityId(lastInvoice.get().getId(), utilityId);
            if (detail.isPresent()) {
                return detail.get().getNewIndex();
            }
        }

        // Cách 2: Nếu chưa có hóa đơn nào (Tháng đầu tiên), lấy từ ContractUtility
        ContractUtility cu = contract.getContractUtilities().stream()
                .filter(u -> u.getUtility().getId().equals(utilityId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utility not defined in contract"));

        return cu.getStartIndex();
    }
}