package com.trandnquang.roomie.repo;

import com.trandnquang.roomie.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Long> {

    // Get all active services to display in "Create Contract" form
    List<Utility> findByIsActiveTrue();
}