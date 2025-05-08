package com.system.app.repositories;

import com.system.app.entities.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("SELECT d FROM Department d WHERE LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :departmentName, '%'))")
    List<Department> findByDepartmentNameLike(String departmentName, Sort sort);

    @Query("select d from Department d where d.isActive = true")
    List<Department> getAllActive();

    Department findByDepartmentName(String departmentName);
}