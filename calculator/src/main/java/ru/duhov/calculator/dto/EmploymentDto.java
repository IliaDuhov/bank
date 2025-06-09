package ru.duhov.calculator.dto;

import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Position;

import java.math.BigDecimal;

public class EmploymentDto {

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}