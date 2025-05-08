package com.system.app.repositories;

import com.system.app.entities.Overtimerecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimerecordRepository extends JpaRepository<Overtimerecord, Integer> {
    void deleteByOvertimeID_Id(Integer overtimeId);

    boolean existsByOvertimeID_Id(Integer overtimeId);

    boolean existsByEmployeeID_IdAndOvertimeID_Id(Integer employeeId, Integer overtimeId);

    @Query("SELECT orr FROM Overtimerecord orr " +
            "JOIN FETCH orr.overtimeID o " +
            "WHERE orr.employeeID.id = :employeeId " +
            "AND FUNCTION('YEAR', o.overtimeDate) = :year " +
            "AND FUNCTION('MONTH', o.overtimeDate) = :month")
    List<Overtimerecord> findByEmployeeIdAndMonthAndYear(@Param("employeeId") Integer employeeId,
                                                         @Param("year") Integer year,
                                                         @Param("month") Integer month);

    @Query("SELECT orr FROM Overtimerecord orr " +
            "JOIN FETCH orr.overtimeID o " +
            "WHERE orr.employeeID.id = :employeeId " +
            "AND FUNCTION('YEAR', o.overtimeDate) = :year")
    List<Overtimerecord> findByEmployeeIdAndYear(@Param("employeeId") Integer employeeId,
                                                 @Param("year") Integer year);
}