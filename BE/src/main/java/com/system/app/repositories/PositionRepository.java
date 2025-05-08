package com.system.app.repositories;

import com.system.app.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    @Query("SELECT p FROM Position p WHERE :positionName IS NULL OR p.positionName LIKE %:positionName%")
    List<Position> findByPositionName(@Param("positionName") String positionName);

    Long countByDepartmentID_id(Integer departmentID);

    boolean existsByPositionNameIgnoreCase(String positionName);

    List<Position> findByPositionNameLike(String positionName);
}