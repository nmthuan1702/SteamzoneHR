package com.system.app.repositories;

import com.system.app.entities.Contract;
import com.system.app.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Page<Contract> findByEmployeeID_FullNameContaining(String keyword, Pageable pageable);

    @Query("SELECT c FROM Contract c WHERE c.employeeID.id = :employeeId " +
            "AND c.isActive = true " +
            "AND ((c.startDate <= :endDate AND c.endDate >= :startDate) " +
            "OR (c.startDate <= :startDate AND c.endDate >= :endDate))")
    Contract findActiveContractForEmployee(Integer employeeId, Instant startDate, Instant endDate);

    @Query("SELECT c FROM Contract c WHERE c.isActive = true " +
            "AND c.employeeID.id = :employeeId " +  // Thêm điều kiện employeeId
            "AND (" +
            "    (FUNCTION('YEAR', c.startDate) = :year AND FUNCTION('MONTH', c.startDate) = :month) " +
            "    OR (FUNCTION('YEAR', c.endDate) = :year AND FUNCTION('MONTH', c.endDate) = :month) " +
            "    OR (FUNCTION('YEAR', c.startDate) <= :year AND FUNCTION('YEAR', c.endDate) >= :year " +
            "        AND FUNCTION('MONTH', c.startDate) <= :month AND FUNCTION('MONTH', c.endDate) >= :month)" +
            ")")
    List<Contract> getAllContractsByMonthAndYear(@Param("employeeId") Integer employeeId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT c FROM Contract c WHERE c.isActive = true " +
            "AND c.employeeID.id = :employeeId " +
            "AND (FUNCTION('YEAR', c.startDate) = :year " +
            "     OR FUNCTION('YEAR', c.endDate) = :year " +
            "     OR (FUNCTION('YEAR', c.startDate) <= :year AND FUNCTION('YEAR', c.endDate) >= :year))")
    List<Contract> getAllContractsByYear(@Param("employeeId") Integer employeeId, @Param("year") int year);


    @Query("SELECT c FROM Contract c WHERE c.isActive = true " +
            "AND c.employeeID.id = :employeeId " +
            "AND (" +
            "    FUNCTION('YEAR', c.startDate) = :year " +
            "    OR FUNCTION('YEAR', c.endDate) = :year " +
            "    OR (FUNCTION('YEAR', c.startDate) <= :year AND FUNCTION('YEAR', c.endDate) >= :year)" +
            ") ORDER BY c.id DESC")
    Optional<Contract> getContractWithLargestIdByYear(@Param("employeeId") Integer employeeId, @Param("year") int year);

    @Query("SELECT c FROM Contract c WHERE c.employeeID.id = :employeeID " +
            "AND (" +
            "    (FUNCTION('YEAR', c.startDate) < :year OR (FUNCTION('YEAR', c.startDate) = :year AND FUNCTION('MONTH', c.startDate) <= :month)) " +
            "    AND " +
            "    (FUNCTION('YEAR', c.endDate) > :year OR (FUNCTION('YEAR', c.endDate) = :year AND FUNCTION('MONTH', c.endDate) >= :month))" +
            ") " +
            "ORDER BY c.id DESC")
    Optional<Contract> getContractWithLargestIdByMonthAndYear(@Param("employeeID") Integer employeeID,
                                                              @Param("month") int month,
                                                              @Param("year") int year);

}