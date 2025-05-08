package com.system.app.services;

import com.system.app.dtos.AllowanceDto;
import com.system.app.entities.Allowance;
import com.system.app.entities.Employee;
import com.system.app.entities.Employeeallowance;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.AllowanceModel;
import com.system.app.repositories.AllowanceRepository;
import com.system.app.repositories.EmployeeRepository;
import com.system.app.repositories.EmployeeallowanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class AllowanceService {
    @Autowired
    private AllowanceRepository allowanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeallowanceRepository employeeallowanceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<AllowanceDto> getAllAllowances(String keyword, Pageable pageable) {
        Page<Allowance> allowances = (keyword == null || keyword.isEmpty())
                ? allowanceRepository.findAll(pageable)
                : allowanceRepository.findByAllowanceNameContainingIgnoreCase(keyword, pageable);
        return allowances.map(allowance -> modelMapper.map(allowance, AllowanceDto.class));
    }

    public AllowanceDto createAllowance(AllowanceModel allowanceModel) {
        if (allowanceModel.getStartDate().isAfter(allowanceModel.getEndDate())) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước hoặc bằng thời gian kết thúc.");
        }

        Allowance allowance = new Allowance();
        allowance.setAllowanceName(allowanceModel.getAllowanceName());
        allowance.setAmount(allowanceModel.getAmount());
        allowance = allowanceRepository.save(allowance);

        Set<Employeeallowance> employeeAllowances = new HashSet<>();
        for (Integer employeeId : allowanceModel.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));

            Employeeallowance employeeAllowance = new Employeeallowance();
            employeeAllowance.setAllowanceID(allowance);
            employeeAllowance.setEmployeeID(employee);
            employeeAllowance.setAmount(allowance.getAmount());
            employeeAllowance.setStartDate(allowanceModel.getStartDate());
            employeeAllowance.setEndDate(allowanceModel.getEndDate());
            employeeAllowances.add(employeeAllowance);
        }

        employeeallowanceRepository.saveAll(employeeAllowances);
        return modelMapper.map(allowance, AllowanceDto.class);
    }

    // Cập nhật phụ cấp
    @Transactional
    public AllowanceDto updateAllowance(Integer id, AllowanceModel allowanceModel) {
        if (allowanceModel.getStartDate().isAfter(allowanceModel.getEndDate())) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước hoặc bằng thời gian kết thúc.");
        }


        Allowance allowance = allowanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phụ cấp với ID: " + id));

        allowance.setAllowanceName(allowanceModel.getAllowanceName());
        allowance.setAmount(allowanceModel.getAmount());
        allowance = allowanceRepository.save(allowance);

        Set<Employeeallowance> existingEmployeeAllowances = employeeallowanceRepository.findByAllowanceID_Id(id);
        if (!existingEmployeeAllowances.isEmpty()) {
            employeeallowanceRepository.deleteByAllowanceID_Id(id);
        }

        Set<Employeeallowance> employeeAllowances = new HashSet<>();
        for (Integer employeeId : allowanceModel.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));

            boolean exists = employeeallowanceRepository.existsByEmployeeID_IdAndAllowanceID_Id(employeeId, allowance.getId());

            if (!exists) {
                Employeeallowance employeeAllowance = new Employeeallowance();
                employeeAllowance.setAllowanceID(allowance);
                employeeAllowance.setEmployeeID(employee);
                employeeAllowance.setStartDate(allowanceModel.getStartDate());
                employeeAllowance.setEndDate(allowanceModel.getEndDate());
                employeeAllowance.setAmount(allowance.getAmount());
                employeeAllowances.add(employeeAllowance);
            }
        }

        if (!employeeAllowances.isEmpty()) {
            employeeallowanceRepository.saveAll(employeeAllowances);
        }

        return modelMapper.map(allowance, AllowanceDto.class);
    }



    @Transactional
    public void deleteAllowance(Integer id) {
        if (!allowanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Allowance not found with ID: " + id);
        }

        allowanceRepository.deleteById(id);
    }
}
