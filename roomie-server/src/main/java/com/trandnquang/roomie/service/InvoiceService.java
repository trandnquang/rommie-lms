package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.invoice.*;
import com.trandnquang.roomie.entity.*;
import com.trandnquang.roomie.model.enums.ContractStatus;
import com.trandnquang.roomie.model.enums.InvoiceStatus;
import com.trandnquang.roomie.model.enums.PricingType;
import com.trandnquang.roomie.repo.*;
import com.trandnquang.roomie.util.CalculatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceCreationRequest request) {
        log.info("Starting invoice creation for Contract ID: {} - {}/{}",
                request.getContractId(), request.getMonth(), request.getYear());

        // ---------------------------------------------------------
        // 1. VALIDATE: CHECK DUPLICATE (LOGIC MỚI)
        // ---------------------------------------------------------
        // Lấy tất cả hóa đơn của tháng này
        List<Invoice> existingInvoices = invoiceRepository.findByContractIdAndMonthAndYear(
                request.getContractId(), request.getMonth(), request.getYear());

        // Nếu tồn tại bất kỳ hóa đơn nào KHÁC trạng thái CANCELLED -> Chặn lại.
        // Nghĩa là: Nếu chỉ có hóa đơn CANCELLED thì vẫn cho tạo tiếp.
        boolean hasActiveInvoice = existingInvoices.stream()
                .anyMatch(inv -> inv.getStatus() != InvoiceStatus.CANCELLED);

        if (hasActiveInvoice) {
            throw new RuntimeException("Hóa đơn tháng này đã tồn tại và đang có hiệu lực (Chưa hủy).");
        }

        // ---------------------------------------------------------
        // [QUAN TRỌNG] ĐÃ XÓA ĐOẠN check existsBy... CŨ Ở ĐÂY ĐỂ TRÁNH CONFLICT
        // ---------------------------------------------------------

        // 2. Lấy thông tin Hợp đồng
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
             throw new RuntimeException("Hợp đồng không hoạt động, không thể tạo hóa đơn.");
        }

        // 3. Khởi tạo Header
        Invoice invoice = Invoice.builder()
                .contract(contract)
                .invoiceCode("INV-" + request.getYear() + String.format("%02d", request.getMonth()) + "-" + contract.getRoom().getRoomNumber())
                .month(request.getMonth())
                .year(request.getYear())
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(5))
                .status(InvoiceStatus.DRAFT)
                .paidAmount(BigDecimal.ZERO)
                .build();

        // Map input readings (UtilityId -> NewIndex)
        Map<Long, BigDecimal> readingsMap = request.getReadings() != null ?
                request.getReadings().stream().collect(Collectors.toMap(
                        InvoiceCreationRequest.UtilityReading::getUtilityId,
                        InvoiceCreationRequest.UtilityReading::getNewIndex
                )) : Map.of();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceDetail> details = new ArrayList<>();

        // 4. TÍNH TIỀN PHÒNG (RENT)
        BigDecimal rentPrice = contract.getRentPrice();
        InvoiceDetail rentDetail = InvoiceDetail.builder()
                .invoice(invoice)
                .utilityName("Tiền thuê phòng tháng " + request.getMonth())
                .totalPrice(rentPrice)
                .description("Giá thuê cố định")
                .build();
        details.add(rentDetail);
        totalAmount = totalAmount.add(rentPrice);

        // 5. TÍNH TIỀN DỊCH VỤ (PRICE LOCKING)
        for (ContractUtility cu : contract.getContractUtilities()) {
            Utility utility = cu.getUtility();

            BigDecimal lockedPrice = cu.getLockedPrice();
            var lockedTierConfig = cu.getLockedTierConfig();

            BigDecimal amount = BigDecimal.valueOf(cu.getAmount());
            BigDecimal finalPrice = BigDecimal.ZERO;
            BigDecimal oldIndex = BigDecimal.ZERO;
            BigDecimal newIndex = BigDecimal.ZERO;
            String description = "";

            switch (utility.getPricingType()) {
                case TIERED:
                case FLAT:
                    // Lấy chỉ số cũ
                    oldIndex = getOldIndex(contract, utility.getId(), request.getMonth(), request.getYear(), cu.getStartIndex());

                    // Lấy chỉ số mới
                    newIndex = readingsMap.getOrDefault(utility.getId(), oldIndex);

                    if (newIndex.compareTo(oldIndex) < 0) {
                        throw new RuntimeException("Chỉ số mới nhỏ hơn chỉ số cũ tại dịch vụ: " + utility.getName());
                    }

                    BigDecimal usage = newIndex.subtract(oldIndex);

                    if (PricingType.TIERED.equals(utility.getPricingType())) {
                        finalPrice = CalculatorUtils.calculateTieredCost(usage, lockedTierConfig);
                        description = String.format("Bậc thang (CS: %s - %s)", oldIndex, newIndex);
                    } else {
                        finalPrice = CalculatorUtils.mul(usage, lockedPrice);
                        description = String.format("Đồng giá (CS: %s - %s)", oldIndex, newIndex);
                    }
                    break;

                case FIXED:
                    finalPrice = CalculatorUtils.mul(lockedPrice, amount);
                    description = "Phí cố định";
                    break;

                default:
                     finalPrice = CalculatorUtils.mul(lockedPrice, amount);
                     description = "Theo số lượng: " + amount;
                     break;
            }

            InvoiceDetail detail = InvoiceDetail.builder()
                    .invoice(invoice)
                    .utility(utility)
                    .utilityName(utility.getName())
                    .oldIndex(oldIndex)
                    .newIndex(newIndex)
                    .appliedPrice(lockedPrice)
                    .totalPrice(finalPrice)
                    .description(description)
                    .build();

            details.add(detail);
            totalAmount = totalAmount.add(finalPrice);
        }

        // 6. Lưu xuống DB
        invoice.setTotalAmount(totalAmount);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        details.forEach(d -> d.setInvoice(savedInvoice));
        invoiceDetailRepository.saveAll(details);

        return mapToResponse(savedInvoice);
    }

    // --- HELPER: Truy vết chỉ số cũ ---
    private BigDecimal getOldIndex(Contract contract, Long utilityId, int currentMonth, int currentYear, BigDecimal contractStartIndex) {
        // Gọi Repo với PageRequest(0, 1) để lấy 1 bản ghi mới nhất
        List<Invoice> oldInvoices = invoiceRepository.findLatestInvoicesBefore(
                contract.getId(), currentMonth, currentYear, PageRequest.of(0, 1));

        if (oldInvoices.isEmpty()) {
            return contractStartIndex; // Tháng đầu tiên -> Lấy từ HĐ
        }

        Invoice previousInvoice = oldInvoices.get(0);
        return invoiceDetailRepository.findByInvoiceIdAndUtilityId(previousInvoice.getId(), utilityId)
                .map(InvoiceDetail::getNewIndex)
                .orElse(contractStartIndex); // Trường hợp dịch vụ này mới đăng ký thêm sau này
    }

    /**
     * 2. LẤY CHI TIẾT HÓA ĐƠN
     */
    public InvoiceResponse getInvoiceDetail(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));

        // Convert Entity -> DTO
        return mapToResponse(invoice);
    }

    /**
     * 3. LẤY DANH SÁCH HÓA ĐƠN THEO HỢP ĐỒNG
     * (Dùng cho Tenant xem lịch sử đóng tiền)
     */
    public List<InvoiceResponse> getInvoicesByContract(Long contractId) {
        // Gọi Repo lấy list đã sắp xếp giảm dần theo thời gian
        List<Invoice> invoices = invoiceRepository.findByContractIdOrderByYearDescMonthDesc(contractId);

        // Convert List<Entity> -> List<DTO>
        return invoices.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 4. HỦY HÓA ĐƠN
     * (Chỉ cho phép hủy nếu chưa thanh toán hoặc thanh toán lỗi)
     */
    @Transactional
    public void cancelInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));

        // Business Rule: Không được hủy hóa đơn đã thanh toán xong (PAID) hoặc trả 1 phần (PARTIAL)
        // Muốn hủy phải hoàn tiền (Logic hoàn tiền phức tạp hơn, tạm thời chặn ở đây)
        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.PARTIAL) {
            throw new RuntimeException("Không thể hủy hóa đơn đã phát sinh thanh toán. Vui lòng liên hệ Admin.");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoiceRepository.save(invoice);
    }

    // --- HELPER: MAPPER (Cần đảm bảo hàm này có trong class) ---
    // (Tôi gửi lại để bạn đối chiếu, nếu có rồi thì không cần copy lại)
    private InvoiceResponse mapToResponse(Invoice invoice) {
        // Tính toán số tiền còn lại = Tổng - Đã trả
        BigDecimal remaining = invoice.getTotalAmount().subtract(
                invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount()
        );

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceCode(invoice.getInvoiceCode())
                // Flatten data từ các bảng cha
                .roomNumber(invoice.getContract().getRoom().getRoomNumber())
                .tenantName(invoice.getContract().getTenant().getFullName())

                .month(invoice.getMonth())
                .year(invoice.getYear())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())

                .totalAmount(invoice.getTotalAmount())
                .paidAmount(invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount())
                .remainingAmount(remaining)
                .status(invoice.getStatus())

                // Map chi tiết hóa đơn (InvoiceDetail -> InvoiceDetailResponse)
                .details(invoice.getDetails().stream().map(d -> InvoiceDetailResponse.builder()
                        .serviceName(d.getUtilityName())
                        .oldIndex(d.getOldIndex())
                        .newIndex(d.getNewIndex())
                        .usageAmount(d.getNewIndex().subtract(d.getOldIndex()))
                        .appliedPrice(d.getAppliedPrice())
                        .totalPrice(d.getTotalPrice())
                        .description(d.getDescription())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}