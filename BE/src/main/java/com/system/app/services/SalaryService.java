package com.system.app.services;

import com.system.app.dtos.SalaryDTO;
import com.system.app.entities.*;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private LeaverecordRepository leaverecordRepository;

    @Autowired
    private EmployeeallowanceRepository employeeAllowanceRepository;

    @Autowired
    private OvertimerecordRepository overtimerecordRepository;

    public List<SalaryDTO> getAllSalaries(Integer year, Integer month) {
        List<SalaryDTO> salaries = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            SalaryDTO salaryDTO = staticsEmployee(employee.getId(), year, month);
            if (salaryDTO != null) {
                salaries.add(salaryDTO);
            }
        }
        return salaries;
    }

    // Tính lương cho một nhân viên cụ thể
    public SalaryDTO staticsEmployee(Integer employeeId, Integer year, Integer month) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return null;
        }
        Long baseSalary = 0L;
        Long allowance = 0L;
        Long overtimeHours = 0L;
        Long overtimeMoneys = 0L;
        Long totalBaseSalary = 0L;
        double leaveDays = 0.0;
        Long totalSalary = 0L;
        Long workDays = 0L;
        if (month == null) {
            Contract contract = contractRepository.getContractWithLargestIdByYear(employeeId, year)
                    .orElse(null);  // Trả về null nếu không tìm thấy hợp đồng
            if (contract == null) {
                return null;
            }
            baseSalary = contract.getSalary();

            List<Employeeallowance> employeeallowances = employeeAllowanceRepository.findByEmployeeIDAndYearBetween(employeeId, year);
            for (Employeeallowance employeeallowance : employeeallowances) {
                LocalDate startDate = employeeallowance.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = employeeallowance.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

                long months = 0L;
                if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth()) {
                    months = 1;
                } else {
                    months = ChronoUnit.MONTHS.between(startDate, endDate);
                }

                allowance += months * employeeallowance.getAmount();
            }

            List<Overtimerecord> overtimerecords = overtimerecordRepository.findByEmployeeIdAndYear(employeeId, year);
            for (Overtimerecord overtimerecord : overtimerecords) {
                long overtimeDurationInHours = Duration.between(overtimerecord.getOvertimeID().getStartTime(), overtimerecord.getOvertimeID().getEndTime()).toHours();
                overtimeHours += overtimeDurationInHours;
                overtimeMoneys += overtimeDurationInHours * overtimerecord.getOvertimeID().getHourlyRate();
            }

            List<Contract> contracts = contractRepository.getAllContractsByYear(employeeId, year);
            for (Contract con : contracts) {
                LocalDate startDate = con.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = con.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

                long months = ChronoUnit.MONTHS.between(startDate, endDate);
                workDays+=months;
                totalBaseSalary += months * con.getSalary();
            }

            List<Leaverecord> leaverecords = leaverecordRepository.findByEmployeeID_IDAndYear(employeeId, year);
            for (Leaverecord leaverecord : leaverecords) {
                leaveDays += leaverecord.getQuantity();
            }

            totalSalary += totalBaseSalary + allowance + overtimeMoneys;

        } else {
            Contract contract = contractRepository.getContractWithLargestIdByMonthAndYear(employeeId, month, year)
                    .orElse(null);
            if(contract == null) {
                return null;
            }
            baseSalary = contract.getSalary();
            totalBaseSalary += contract.getSalary();

            List<Employeeallowance> employeeallowances = employeeAllowanceRepository.findByEmployeeIDAndMonthYearBetween(employeeId, month, year);
            for (Employeeallowance employeeallowance : employeeallowances) {
                LocalDate startDate = employeeallowance.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = employeeallowance.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

                long months = 0L;
                if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth()) {
                    months = 1;
                } else {
                    months = ChronoUnit.MONTHS.between(startDate, endDate);
                }

                allowance += months * employeeallowance.getAmount();
            }

            List<Overtimerecord> overtimerecords = overtimerecordRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year);
            for (Overtimerecord overtimerecord : overtimerecords) {
                long overtimeDurationInHours = Duration.between(overtimerecord.getOvertimeID().getStartTime(), overtimerecord.getOvertimeID().getEndTime()).toHours();
                overtimeHours += overtimeDurationInHours;
                overtimeMoneys += overtimeDurationInHours * overtimerecord.getOvertimeID().getHourlyRate();
            }

            List<Contract> contracts = contractRepository.getAllContractsByMonthAndYear(employeeId, month, year);

            List<Leaverecord> leaverecords = leaverecordRepository.findByEmployeeID_IDAndMonthYear(employeeId, month, year);
            for (Leaverecord leaverecord : leaverecords) {
                leaveDays += leaverecord.getQuantity();
            }
            totalSalary += totalBaseSalary + allowance + overtimeMoneys;
        }

        return new SalaryDTO(employee.getFullName(), employee.getAvatarURL(), employee.getPositionID().getPositionName(),
                employee.getPositionID().getDepartmentID().getDepartmentName(),
                workDays ,leaveDays, allowance, overtimeHours, overtimeMoneys, baseSalary, totalBaseSalary, totalSalary);
    }

}

