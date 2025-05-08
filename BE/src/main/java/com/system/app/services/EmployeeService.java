package com.system.app.services;

import com.system.app.dtos.EmployeeCompensationDetailsDto;
import com.system.app.dtos.EmployeeDto;
import com.system.app.dtos.EmployeeOvertimeDetailsDto;
import com.system.app.dtos.PositionDto;
import com.system.app.entities.Employee;
import com.system.app.entities.Position;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.EmployeeModel;
import com.system.app.repositories.EmployeeRepository;
import com.system.app.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    public Page<EmployeeDto> getAllEmployees(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Employee> employeePage = employeeRepository.findByKeyword(keyword, pageable);
        return employeePage.map(this::convertToDto);
    }

    public EmployeeDto getEmployeeById(int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Không tìm thấy nhân viên với id: " + id));
        return convertToDto(employee);
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeModel employeeModel) {
        validateDuplicate(employeeModel);
        Employee employee = new Employee();
        convertToEntity(employeeModel, employee);
        return convertToDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto updateEmployee(Integer id, EmployeeModel employeeModel) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với id: " + id));

        if(!employeeModel.getEmail().equals(existingEmployee.getEmail())){
            validateDuplicateEmail(employeeModel);
        }
        if(!employeeModel.getPhoneNumber().equals(existingEmployee.getPhoneNumber())){
            validateDuplicatePhoneNumber(employeeModel);
        }
        if(!employeeModel.getIDCardNumber().equals(existingEmployee.getIDCardNumber())){
            validateDuplicateIDCard(employeeModel);
        }

        convertToEntity(employeeModel, existingEmployee);
        return convertToDto(employeeRepository.save(existingEmployee));
    }

    @Transactional
    public EmployeeDto toggleStatus(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với id: " + id));

        // Đảo ngược trạng thái isActive
        employee.setIsActive(!employee.getIsActive());

        // Lưu lại thay đổi
        Employee updatedEmployee = employeeRepository.save(employee);

        return convertToDto(updatedEmployee);
    }


    // Xóa nhân viên
    @Transactional
    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với id: " + id));
        employeeRepository.delete(employee);
    }

    // Hàm kiểm tra trùng email, phoneNumber, iDCardNumber khi thêm/cập nhật nhân viên
    private void validateDuplicate(EmployeeModel employeeModel, Integer... excludeId) {
        // Kiểm tra trùng email
        Optional<Employee> existingEmail = employeeRepository.findByEmail(employeeModel.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Kiểm tra trùng số điện thoại
        Optional<Employee> existingPhoneNumber = employeeRepository.findByPhoneNumber(employeeModel.getPhoneNumber());
        if (existingPhoneNumber.isPresent() && !existingPhoneNumber.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        // Kiểm tra trùng số CMND/CCCD
        Optional<Employee> existingIDCard = employeeRepository.findByiDCardNumber(employeeModel.getIDCardNumber());
        if (existingIDCard.isPresent() && !existingIDCard.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Số CMND/CCCD đã tồn tại");
        }
    }

    // Kiểm tra trùng email
    private void validateDuplicateEmail(EmployeeModel employeeModel, Integer... excludeId) {
        Optional<Employee> existingEmail = employeeRepository.findByEmail(employeeModel.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
    }

    // Kiểm tra trùng số điện thoại
    private void validateDuplicatePhoneNumber(EmployeeModel employeeModel, Integer... excludeId) {
        Optional<Employee> existingPhoneNumber = employeeRepository.findByPhoneNumber(employeeModel.getPhoneNumber());
        if (existingPhoneNumber.isPresent() && !existingPhoneNumber.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
    }

    // Kiểm tra trùng số CMND/CCCD
    private void validateDuplicateIDCard(EmployeeModel employeeModel, Integer... excludeId) {
        Optional<Employee> existingIDCard = employeeRepository.findByiDCardNumber(employeeModel.getIDCardNumber());
        if (existingIDCard.isPresent() && !existingIDCard.get().getId().equals(excludeId)) {
            throw new IllegalArgumentException("Số CMND/CCCD đã tồn tại");
        }
    }



    // Chuyển đổi từ EmployeeModel sang Employee entity
    private void convertToEntity(EmployeeModel model, Employee entity) {
        entity.setFullName(model.getFullName());
        entity.setGender(model.getGender());
        entity.setBirthDate(model.getBirthDate());
        entity.setEmail(model.getEmail());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setIDCardNumber(model.getIDCardNumber());
        entity.setAddress(model.getAddress());
        entity.setAvatarURL(model.getAvatarURL());
        Position position = positionRepository.findById(model.getPositionID()).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ với id: "));
        entity.setPositionID(position);
        entity.setIsActive(true); // Hoặc có thể tùy chỉnh theo yêu cầu
    }

    // Chuyển đổi từ Employee entity sang EmployeeDto
    private EmployeeDto convertToDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                new PositionDto(employee.getPositionID().getId(),
                        employee.getPositionID().getPositionName(),
                        employee.getPositionID().getDepartmentID().getId(),
                        employee.getPositionID().getDepartmentID().getDepartmentName()),
                employee.getFullName(),
                employee.getGender(),
                employee.getBirthDate(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getIDCardNumber(),
                employee.getAddress(),
                employee.getAvatarURL(),
                employee.getIsActive());
    }

    public List<EmployeeOvertimeDetailsDto> getEmployee(Integer years,Integer months) {
        List<EmployeeOvertimeDetailsDto> dtoList = new ArrayList<>();
        List<Object[]> results=employeeRepository.getEmployeeOvertimeDetails(years,months);
        for (Object[] result : results) {
            String fullName = (String) result[0];
            String departmentName = (String) result[1];
            String positionName = (String) result[2];
            Double dateLeave = ((Float) result[3]).doubleValue();          // Convert Float to Double
            Long totalOvertimeHours = ((BigDecimal) result[4]).longValue();

            EmployeeOvertimeDetailsDto dto = new EmployeeOvertimeDetailsDto(fullName, departmentName, positionName,
                    dateLeave,  totalOvertimeHours);
            dtoList.add(dto);
        }
        return dtoList;
    }



    public List<EmployeeCompensationDetailsDto> getSalary(Integer years,Integer months) {
        List<EmployeeCompensationDetailsDto> dtoList = new ArrayList<>();
        List<Object[]> results=employeeRepository.getEmployeeCompensationDetails(years,months);
        for (Object[] result : results) {
            String fullname=(String) result[0];
            String departmentName=(String) result[1];
            String positionName=(String) result[2];
            Long salary=(Long) result[3];
            Long totalAllowance=((BigDecimal) result[4]).longValue();
            Long totalOvertimeHours=((BigDecimal) result[5]).longValue();
            Long totalSalary=((BigDecimal) result[6]).longValue();
            EmployeeCompensationDetailsDto dto =new EmployeeCompensationDetailsDto(fullname,departmentName,positionName
            ,salary,totalAllowance,totalOvertimeHours,totalSalary);
         dtoList.add(dto);
        }
        return dtoList;
    }

}

