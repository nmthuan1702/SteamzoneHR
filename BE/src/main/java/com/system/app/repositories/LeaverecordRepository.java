package com.system.app.repositories;

import com.system.app.entities.Leaverecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface LeaverecordRepository extends JpaRepository<Leaverecord, Integer> {
    Page<Leaverecord> findByEmployeeID_FullNameContainingAndFromDateBetween(
            String keyword, Instant fromDate, Instant toDate, Pageable pageable);

    // Tìm kiếm chỉ theo khoảng thời gian
    Page<Leaverecord> findByFromDateBetween(Instant fromDate, Instant toDate, Pageable pageable);

    Page<Leaverecord> findByEmployeeID_FullNameContaining(String keyword, Pageable pageable);

    @Query("SELECT lv FROM Leaverecord lv WHERE lv.employeeID.id = :id " +
            "AND FUNCTION('YEAR', lv.fromDate) = :year " +
            "AND FUNCTION('MONTH', lv.fromDate) = :month")
    List<Leaverecord> findByEmployeeID_IDAndMonthYear(int id, int month, int year);


    @Query("SELECT lv FROM Leaverecord lv WHERE lv.employeeID.id = :id " +
            "AND FUNCTION('YEAR', lv.fromDate) = :year")
    List<Leaverecord> findByEmployeeID_IDAndYear(int id, int year);



}