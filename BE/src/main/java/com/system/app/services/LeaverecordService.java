package com.system.app.services;

import com.system.app.dtos.LeaverecordDto;
import com.system.app.entities.Leaverecord;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.LeaverecordModel;
import com.system.app.repositories.LeaverecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LeaverecordService {
    @Autowired
    private LeaverecordRepository leaverecordRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<LeaverecordDto> getAllLeaverecords(String keyword, Instant fromDate, Instant toDate, Pageable pageable) {
        Page<Leaverecord> leaverecords;

        if (fromDate != null && toDate != null) {
            if (keyword != null && !keyword.isEmpty()) {
                leaverecords = leaverecordRepository.findByEmployeeID_FullNameContainingAndFromDateBetween(
                        keyword, fromDate, toDate, pageable);
            } else {
                leaverecords = leaverecordRepository.findByFromDateBetween(fromDate, toDate, pageable);
            }
        } else if (keyword != null && !keyword.isEmpty()) {
            leaverecords = leaverecordRepository.findByEmployeeID_FullNameContaining(keyword, pageable);
        } else {
            leaverecords = leaverecordRepository.findAll(pageable);
        }

        return leaverecords.map(record -> modelMapper.map(record, LeaverecordDto.class));
    }

    public LeaverecordDto getLeaverecordById(Integer id) {
        Leaverecord leaverecord = leaverecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leaverecord not found with ID: " + id));
        return modelMapper.map(leaverecord, LeaverecordDto.class);
    }

    public LeaverecordDto createLeaverecord(LeaverecordModel leaverecordModel) {
        Leaverecord leaverecord = modelMapper.map(leaverecordModel, Leaverecord.class);
        leaverecord = leaverecordRepository.save(leaverecord);
        return modelMapper.map(leaverecord, LeaverecordDto.class);
    }

    public void deleteLeaverecord(Integer id) {
        leaverecordRepository.deleteById(id);
    }
}
