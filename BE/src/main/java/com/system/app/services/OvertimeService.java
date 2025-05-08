package com.system.app.services;

import com.system.app.dtos.OvertimeDto;
import com.system.app.entities.Employee;
import com.system.app.entities.Overtime;
import com.system.app.entities.Overtimerecord;
import com.system.app.exceptions.EntityAlreadyExistsException;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.OvertimeModel;
import com.system.app.repositories.EmployeeRepository;
import com.system.app.repositories.OvertimeRepository;
import com.system.app.repositories.OvertimerecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class OvertimeService {
    @Autowired
    private OvertimeRepository overtimeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OvertimerecordRepository overtimerecordRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<OvertimeDto> getAllOvertimes(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Overtime> overtimes;
        if (startDate != null && endDate != null) {
            overtimes = overtimeRepository.findByOvertimeDateBetween(startDate, endDate, pageable);
        } else {
            overtimes = overtimeRepository.findAll(pageable);
        }
        return overtimes.map(overtime -> modelMapper.map(overtime, OvertimeDto.class));
    }

    public Page<OvertimeDto> getAllOvertime(LocalDate startDate, Pageable pageable) {
        Page<Overtime> overtimes;
        if (startDate != null) {
            overtimes = overtimeRepository.findByOvertimeDate(startDate, pageable);
        } else {
            overtimes = overtimeRepository.findAll(pageable);
        }
        return overtimes.map(overtime -> modelMapper.map(overtime, OvertimeDto.class));
    }

    public OvertimeDto createOvertime(OvertimeModel overtimeModel) {
        if (!overtimeModel.isValidTimes()) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        if (overtimeModel.getOvertimeDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày làm thêm không được là ngày trong quá khứ.");
        }

        if(!overtimeRepository.findByOvertimeDate(overtimeModel.getOvertimeDate()).isEmpty()){
            throw new EntityAlreadyExistsException("Đã có bảng tăng ca trong ngày này, vui lòng kiểm tra lại");
        }

        Overtime overtime = new Overtime();
        LocalDate overtimeDate = overtimeModel.getOvertimeDate();

        overtimeDate = overtimeDate.plusDays(1);
        overtime.setOvertimeDate(overtimeDate);

        overtime.setStartTime(overtimeModel.getStartTime());
        overtime.setEndTime(overtimeModel.getEndTime());
        overtime.setHourlyRate(overtimeModel.getHourlyRate());
        overtime = overtimeRepository.save(overtime);

        Set<Overtimerecord> overtimerecords = new HashSet<>();
        for (Integer employeeId : overtimeModel.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));
            Overtimerecord overtimerecord = new Overtimerecord();
            overtimerecord.setOvertimeID(overtime);
            overtimerecord.setEmployeeID(employee);
            overtimerecords.add(overtimerecord);
        }
        overtimerecordRepository.saveAll(overtimerecords);
        return modelMapper.map(overtime, OvertimeDto.class);
    }

    @Transactional
    public OvertimeDto updateOvertime(Integer id, OvertimeModel overtimeModel) {
        // Kiểm tra thời gian
        if (!overtimeModel.isValidTimes()) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        // Lấy thông tin Overtime từ DB
        Overtime overtime = overtimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tăng ca với ID: " + id));

        LocalDate overtimeDate = overtimeModel.getOvertimeDate();

        overtimeDate = overtimeDate.plusDays(1);
        System.out.println("\n\n\n"+overtimeDate);
        overtime.setOvertimeDate(overtimeDate);
        overtime.setStartTime(overtimeModel.getStartTime());
        overtime.setEndTime(overtimeModel.getEndTime());
        overtime.setHourlyRate(overtimeModel.getHourlyRate());
        overtime = overtimeRepository.save(overtime);

        // Kiểm tra và xóa các Overtimerecord cũ
        overtimerecordRepository.deleteByOvertimeID_Id(id);

        // Tạo mới các bản ghi Overtimerecord, nhưng cần kiểm tra xem đã có bản ghi nào tương ứng chưa
        Set<Overtimerecord> overtimerecords = new HashSet<>();
        for (Integer employeeId : overtimeModel.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên với ID: " + employeeId));

            // Kiểm tra nếu đã có bản ghi trong bảng Overtimerecord
            boolean exists = overtimerecordRepository.existsByEmployeeID_IdAndOvertimeID_Id(employeeId, overtime.getId());

            // Nếu chưa tồn tại, thêm mới
            if (!exists) {
                Overtimerecord overtimerecord = new Overtimerecord();
                overtimerecord.setOvertimeID(overtime);
                overtimerecord.setEmployeeID(employee);
                overtimerecords.add(overtimerecord);
            }
        }

        if (!overtimerecords.isEmpty()) {
            overtimerecordRepository.saveAll(overtimerecords);
        }

        // Trả về DTO đã cập nhật
        return modelMapper.map(overtime, OvertimeDto.class);
    }

    @Transactional
    public void deleteOvertime(Integer id) {
        Overtime overtime = overtimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tăng ca với ID: " + id));

        if (overtime.getOvertimeDate().isBefore(LocalDate.now().minusWeeks(1))) {
            throw new IllegalArgumentException("Không thể xóa tăng ca cũ hơn 1 tuần.");
        }

        overtimeRepository.deleteById(id);
    }
}
