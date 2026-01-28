package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Invoice;
import com.trandnquang.roomie.model.enums.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceCode(String invoiceCode);

    List<Invoice> findByContractIdOrderByYearDescMonthDesc(Long contractId);

    List<Invoice> findByContractIdAndMonthAndYear(Long contractId, Integer month, Integer year);

    List<Invoice> findByStatusIn(List<InvoiceStatus> statuses);

    /**
     * LOGIC TÌM HÓA ĐƠN LIỀN KỀ TRƯỚC ĐÓ (Để lấy chỉ số điện/nước cũ).
     * Logic: Tìm các hóa đơn có (Năm < Năm hiện tại) HOẶC (Năm = Năm hiện tại VÀ Tháng < Tháng hiện tại).
     * Sắp xếp giảm dần theo thời gian.
     * Lưu ý: Không dùng LIMIT 1 trong JPQL. Ta sẽ truyền PageRequest.of(0, 1) từ Service.
     */
    @Query("SELECT i FROM Invoice i WHERE i.contract.id = :contractId " +
           "AND (i.year < :year OR (i.year = :year AND i.month < :month)) " +
           "ORDER BY i.year DESC, i.month DESC")
    List<Invoice> findLatestInvoicesBefore(
            @Param("contractId") Long contractId,
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable
    );
}