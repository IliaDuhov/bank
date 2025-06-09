package ru.duhov.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Position;

import java.math.BigDecimal;

@Data
public class EmploymentDto {

    @NotNull
    private EmploymentStatus employmentStatus;

    @NotBlank
    private String employerINN;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private Position position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;
}