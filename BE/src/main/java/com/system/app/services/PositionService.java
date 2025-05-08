package com.system.app.services;

import com.system.app.dtos.PositionDto;
import com.system.app.entities.Position;
import com.system.app.entities.Department;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.PositionModel;
import com.system.app.repositories.EmployeeRepository;
import com.system.app.repositories.PositionRepository;
import com.system.app.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public PositionDto getPositionById(Integer id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ với id: " + id));
        return convertToDto(position);
    }

    public PositionDto createPosition(PositionModel positionModel) {
        String cleanedPositionName = positionModel.getPositionName().trim().toLowerCase();

        // Kiểm tra trùng tên chức vụ
        if (positionRepository.existsByPositionNameIgnoreCase(cleanedPositionName)) {
            throw new IllegalArgumentException("Tên chức vụ đã tồn tại.");
        }

        Department department = departmentRepository.findById(positionModel.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + positionModel.getDepartmentId()));

        Position position = new Position();
        position.setPositionName(positionModel.getPositionName().trim());
        position.setDepartmentID(department);

        positionRepository.save(position);
        return convertToDto(position);
    }

    public PositionDto updatePosition(Integer id, PositionModel positionModel) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ với id: " + id));

        String cleanedPositionName = positionModel.getPositionName().trim().toLowerCase();

        // Kiểm tra trùng tên chức vụ khi cập nhật
        if (positionRepository.existsByPositionNameIgnoreCase(cleanedPositionName) &&
                !position.getPositionName().equalsIgnoreCase(positionModel.getPositionName())) {
            throw new IllegalArgumentException("Tên chức vụ đã tồn tại.");
        }

        position.setPositionName(positionModel.getPositionName().trim());

        Department department = departmentRepository.findById(positionModel.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + positionModel.getDepartmentId()));

        position.setDepartmentID(department);

        positionRepository.save(position);
        return convertToDto(position);
    }

    @Transactional
    public void deletePosition(Integer id) {
        Long count = employeeRepository.countByPositionID_id(id);

        if (count > 0) {
            throw new IllegalStateException("Không thể xóa chức vụ này vì có nhân viên đang sử dụng nó.");
        }

        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ với id: " + id));

        positionRepository.delete(position);
    }

    public List<PositionDto> searchPositions(String positionName) {
        String cleanedPositionName = "%" + positionName.trim().toLowerCase() + "%";
        List<Position> positions = positionRepository.findByPositionNameLike(cleanedPositionName);

        return positions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PositionDto convertToDto(Position position) {
        PositionDto positionDto = new PositionDto();
        positionDto.setId(position.getId());
        positionDto.setPositionName(position.getPositionName());
        positionDto.setDepartmentId(position.getDepartmentID().getId());
        positionDto.setDepartmentName(position.getDepartmentID().getDepartmentName());
        return positionDto;
    }
}
