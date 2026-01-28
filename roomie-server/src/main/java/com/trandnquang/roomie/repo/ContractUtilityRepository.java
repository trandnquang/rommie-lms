package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.ContractUtility;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractUtilityRepository extends JpaRepository<ContractUtility, Long> {

    // Nên fetch luôn Utility để lấy thông tin giá/tên dịch vụ
    @EntityGraph(attributePaths = {"utility"})
    List<ContractUtility> findByContractId(Long contractId);
}