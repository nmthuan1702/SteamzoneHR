package com.system.app.repositories;

import com.system.app.entities.Employeeallowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EmployeeallowanceRepository extends JpaRepository<Employeeallowance, Integer> {

    void deleteByAllowanceID_Id(Integer allowanceId);

    boolean existsByEmployeeID_IdAndAllowanceID_Id(Integer employeeID, Integer allowanceID);

    Set<Employeeallowance> findByAllowanceID_Id(Integer allowanceID);

    // Truy vấn tìm theo EmployeeID và tháng/năm, sử dụng các hàm MySQL trong native query
    @Query(value = "SELECT * FROM employeeallowances ea " +
            "WHERE ea.employeeID = :employeeID " +
            "AND (" +
            "    (YEAR(ea.startDate) = :year AND MONTH(ea.startDate) = :month) " +
            "    OR (YEAR(ea.endDate) = :year AND MONTH(ea.endDate) = :month) " +
            "    OR (ea.startDate <= LAST_DAY(CONCAT(:year, '-', :month, '-01')) " +
            "        AND ea.endDate >= CONCAT(:year, '-', :month, '-01')) " +
            "    OR (ea.startDate <= CONCAT(:year, '-', :month, '-01') AND ea.endDate >= LAST_DAY(CONCAT(:year, '-', :month, '-01')))" +
            ")", nativeQuery = true)
    List<Employeeallowance> findByEmployeeIDAndMonthYearBetween(Integer employeeID, int month, int year);

    // Truy vấn tìm theo EmployeeID và năm, sử dụng các hàm MySQL trong native query
    @Query(value = "SELECT * FROM employeeallowances ea " +
            "WHERE ea.employeeID = :employeeID " +
            "AND (" +
            "    (ea.startDate <= CONCAT(:year, '-12-31') AND ea.endDate >= CONCAT(:year, '-01-01'))" +
            ")", nativeQuery = true)
    List<Employeeallowance> findByEmployeeIDAndYearBetween(Integer employeeID, int year);
}
