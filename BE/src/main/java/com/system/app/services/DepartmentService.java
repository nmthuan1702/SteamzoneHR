package com.system.app.services;

import com.system.app.dtos.DepartmentDto;
import com.system.app.dtos.PositionDto;
import com.system.app.entities.Department;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.DepartmentModel;
import com.system.app.repositories.DepartmentRepository;
import com.system.app.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    public List<DepartmentDto> getDepartments(String departmentName, String sortBy, Sort.Direction direction) {
        String searchTerm = "%" + departmentName.trim().toLowerCase() + "%";
        Sort sort = Sort.by(direction, sortBy);
        List<Department> departments = departmentRepository.findByDepartmentNameLike(searchTerm, sort);
        return departments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DepartmentDto> getActiveDepartments() {
        List<Department> departments = departmentRepository.getAllActive();
        return departments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + id));
        return convertToDto(department);
    }

    public DepartmentDto createDepartment(DepartmentModel departmentModel) {
        String departmentName = departmentModel.getDepartmentName().trim().toLowerCase();
        validateDuplicate(departmentName);

        Department department = new Department();
        department.setDepartmentName(departmentName);
        department.setIsActive(true);

        departmentRepository.save(department);
        return convertToDto(department);
    }

    public DepartmentDto updateDepartment(Integer id, DepartmentModel departmentModel) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + id));

        String departmentName = departmentModel.getDepartmentName().trim().toLowerCase();
        if (!department.getDepartmentName().equals(departmentName)) {
            validateDuplicate(departmentName, id);
        }

        department.setDepartmentName(departmentName);
        departmentRepository.save(department);
        return convertToDto(department);
    }

    public DepartmentDto toggleDepartmentStatus(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + id));

        department.setIsActive(!department.getIsActive()); // Thay đổi trạng thái isActive
        departmentRepository.save(department);

        return convertToDto(department); // Trả về đối tượng DepartmentDto sau khi thay đổi
    }


    @Transactional
    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ban với id: " + id));

        Long count = positionRepository.countByDepartmentID_id(id);
        if (count > 0) {
            throw new IllegalStateException("Không thể xóa phòng ban này vì có chức vụ đang tham chiếu đến nó.");
        }

        departmentRepository.delete(department);
    }

    private void validateDuplicate(String departmentName, Integer... excludeId) {
        Department existingDepartment = departmentRepository.findByDepartmentName(departmentName);
        if (existingDepartment != null && (excludeId.length == 0 || !existingDepartment.getId().equals(excludeId[0]))) {
            throw new IllegalArgumentException("Tên phòng ban đã tồn tại");
        }
    }

    private DepartmentDto convertToDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(department.getId());
        departmentDto.setDepartmentName(department.getDepartmentName());
        departmentDto.setIsActive(department.getIsActive());
        departmentDto.setPositions(department.getPositions().stream()
                .map(position -> new PositionDto(position.getId(), position.getPositionName(), position.getDepartmentID().getId(), position.getDepartmentID().getDepartmentName()))
                .collect(Collectors.toSet()));
        return departmentDto;
    }
}
