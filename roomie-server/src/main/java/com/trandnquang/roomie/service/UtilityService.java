package com.trandnquang.roomie.service;

import com.trandnquang.roomie.dto.utility.UtilityRequest;
import com.trandnquang.roomie.entity.Utility;
import com.trandnquang.roomie.repo.UtilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilityService {

    private final UtilityRepository utilityRepository;

    // --- 1. CREATE: Tạo Tiện ích mới ---
    @Transactional
    public Utility createUtility(UtilityRequest request) {
        // Mapping thủ công: Request -> Entity
        Utility utility = Utility.builder()
                .name(request.getName())
                .basePrice(request.getBasePrice()) // BigDecimal
                .unit(request.getUnit()) // Đơn vị: kWh, m3, Tháng...
                .pricingType(request.getPricingType()) // ENUM: FLAT, TIERED, FIXED
                .description(request.getDescription())
                .tierConfig(request.getTierConfig()) // Tự động nhận List<TierConfig>
                .isActive(true) // Mặc định là đang hoạt động
                .isDeleted(false)
                .build();

        return utilityRepository.save(utility);
    }

    // --- 2. READ ALL: Lấy danh sách ---
    public List<Utility> getAllUtilities() {
        return utilityRepository.findAll();
    }

    // --- 3. READ ONE: Lấy chi tiết ---
    public Utility getUtilityById(Long id) {
        return utilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utility not found with id: " + id));
    }

    // --- 4. UPDATE: Cập nhật Tiện ích ---
    @Transactional
    public Utility updateUtility(Long id, UtilityRequest request) {
        Utility utility = getUtilityById(id);

        // Cập nhật thông tin
        utility.setName(request.getName());
        utility.setBasePrice(request.getBasePrice());
        utility.setUnit(request.getUnit());
        utility.setPricingType(request.getPricingType());
        utility.setDescription(request.getDescription());

        // Lưu ý: Nếu có field tierConfig (JSON), hãy set ở đây luôn

        return utilityRepository.save(utility);
    }

    // --- 5. DELETE: Xóa mềm ---
    @Transactional
    public void deleteUtility(Long id) {
        Utility utility = getUtilityById(id);
        // Nhờ @SQLDelete trong Entity, lệnh này sẽ update is_deleted = true
        utilityRepository.delete(utility);
    }

    // --- 6. TOGGLE STATUS: Bật/Tắt hoạt động ---
    @Transactional
    public void toggleStatus(Long id) {
        Utility utility = getUtilityById(id);
        utility.setActive(!utility.isActive()); // Đảo ngược trạng thái
        utilityRepository.save(utility);
    }
}