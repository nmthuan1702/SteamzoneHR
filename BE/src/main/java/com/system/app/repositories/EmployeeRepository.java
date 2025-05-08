package com.system.app.repositories;

import com.system.app.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e WHERE " +
            "(:keyword IS NULL OR e.fullName LIKE %:keyword% OR e.email LIKE %:keyword% OR e.phoneNumber LIKE %:keyword%)")
    Page<Employee> findByKeyword(String keyword, Pageable pageable);

    Long countByPositionID_id(int id);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Optional<Employee> findByiDCardNumber(String iDCardNumber);

    @Query(value = """
        SELECT 
            ep.FullName AS fullName,
            dm.DepartmentName AS departmentName,
            pt.PositionName AS positionName,
            lr.Quantity,
            SUM(
                TIMESTAMPDIFF(
                    HOUR, 
                    ot.StartTime, 
                    IF(ot.EndTime < ot.StartTime, ADDTIME(ot.EndTime, '24:00:00'), ot.EndTime)
                )
            ) AS totalOvertimeHours
        FROM Employees ep
        JOIN Positions pt ON pt.PositionID = ep.PositionID
        JOIN Departments dm ON dm.DepartmentID = pt.DepartmentID
        JOIN LeaveRecords lr ON lr.EmployeeID = ep.EmployeeID
        JOIN OvertimeRecords ovt ON ovt.EmployeeID = ep.EmployeeID
        JOIN Overtimes ot ON ot.OvertimeID = ovt.OvertimeID
        WHERE
            YEAR(ot.OvertimeDate) = :year
            AND (:month IS NULL OR MONTH(ot.OvertimeDate) = :month)
        GROUP BY ep.FullName, dm.DepartmentName, pt.PositionName, lr.Quantity
        """, nativeQuery = true)
    List<Object[]> getEmployeeOvertimeDetails(@Param("year") Integer year, @Param("month") Integer month);


    @Query(value = """
    SELECT 
        ep.FullName,
        dm.DepartmentName,
        pt.PositionName,
        ct.Salary,
        SUM(epl.amount),
        SUM(
            TIMESTAMPDIFF(
                HOUR, 
                ot.StartTime, 
                IF(ot.EndTime < ot.StartTime, ADDTIME(ot.EndTime, '24:00:00'), ot.EndTime)
            )
        ) * ot.HourlyRate,
        SUM(ct.Salary) + SUM(epl.amount) + 
        SUM(
            TIMESTAMPDIFF(
                HOUR, 
                ot.StartTime, 
                IF(ot.EndTime < ot.StartTime, ADDTIME(ot.EndTime, '24:00:00'), ot.EndTime)
            ) * ot.HourlyRate
        )
    FROM Employees ep
    JOIN Positions pt ON pt.PositionID = ep.PositionID
    JOIN Departments dm ON dm.DepartmentID = pt.DepartmentID
    JOIN OvertimeRecords ovt ON ovt.EmployeeID = ep.EmployeeID
    JOIN Overtimes ot ON ot.OvertimeID = ovt.OvertimeID
    JOIN Contracts ct ON ct.EmployeeID = ep.EmployeeID
    JOIN EmployeeAllowances epl ON epl.EmployeeID = ep.EmployeeID
    WHERE
            YEAR(ot.OvertimeDate) = :year
            AND (:month IS NULL OR MONTH(ot.OvertimeDate) = :month)
    GROUP BY ep.FullName, dm.DepartmentName, pt.PositionName, ct.Salary
    """, nativeQuery = true)
    List<Object[]> getEmployeeCompensationDetails(@Param("year") Integer year, @Param("month") Integer month);

}