package com.system.app.repositories;

import com.system.app.entities.Overtime;
import com.system.app.entities.Overtimerecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    Page<Overtime> findByOvertimeDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Tìm kiếm theo khoảng thời gian và sắp xếp giảm dần theo id
    Page<Overtime> findByOvertimeDate(LocalDate overtimeDate, Pageable pageable);

    List<Overtime> findByOvertimeDate(LocalDate startDate);
}