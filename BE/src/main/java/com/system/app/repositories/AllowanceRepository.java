package com.system.app.repositories;

import com.system.app.entities.Allowance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {
    Page<Allowance> findByAllowanceNameContainingIgnoreCase(String keyword, Pageable pageable);
}