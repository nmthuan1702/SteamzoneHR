package com.system.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto{
    Integer id;
    PositionDto positionID;
    String fullName;
    Boolean gender;
    LocalDate birthDate;
    String email;
    String phoneNumber;
    String iDCardNumber;
    String address;
    String avatarURL;
    Boolean isActive;
}