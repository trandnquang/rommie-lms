package com.trandnquang.roomie.dto.payment;

import com.trandnquang.roomie.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull(message = "Vui lòng chọn Hóa đơn cần thanh toán")
    private Long invoiceId;

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;

    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    private PaymentMethod paymentMethod; // CASH, BANK_TRANSFER...

    private String transactionCode; // Mã giao dịch ngân hàng
    private String note;
}