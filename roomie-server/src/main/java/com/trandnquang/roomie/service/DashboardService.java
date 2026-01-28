package com.trandnquang.roomie.service;

import com.trandnquang.roomie.model.enums.InvoiceStatus;
import com.trandnquang.roomie.model.enums.RoomStatus;
import com.trandnquang.roomie.repo.InvoiceRepository;
import com.trandnquang.roomie.repo.RoomRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoomRepository roomRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardStats getStats() {
        long totalRooms = roomRepository.count();
        long occupiedRooms = roomRepository.countByPropertyAndStatus(null, RoomStatus.OCCUPIED); // Cần sửa Repo để handle null propertyId nếu muốn count all
        long availableRooms = totalRooms - occupiedRooms;

        // Tính doanh thu tạm tính (đơn giản hóa)
        // Trong thực tế sẽ dùng @Query SUM(...)

        return DashboardStats.builder()
                .totalRooms(totalRooms)
                .occupiedRooms(occupiedRooms)
                .availableRooms(availableRooms)
                .occupancyRate(totalRooms == 0 ? 0 : (double) occupiedRooms / totalRooms * 100)
                .build();
    }

    @Data
    @Builder
    public static class DashboardStats {
        private long totalRooms;
        private long occupiedRooms;
        private long availableRooms;
        private double occupancyRate;
        // Thêm revenue, unpaidInvoices... sau
    }
}