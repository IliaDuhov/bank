package ru.duhov.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employment presentation")
public class EmploymentDto {

    @NotNull
    @Schema(description = "Status of employee", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;

    @NotBlank
    @Schema(description = "Taxpayer Identification Number", example = "123456789012")
    private String employerINN;

    @NotNull
    @Schema(description = "Salary of employee", example = "60000")
    private BigDecimal salary;

    @NotNull
    @Schema(description = "Position of employee", example = "MANAGER")
    private Position position;

    @NotNull
    @Schema(description = "Total work experience in months of employee", example = "20")
    private Integer workExperienceTotal;

    @NotNull
    @Schema(description = "Current work experience in months of employee", example = "10")
    private Integer workExperienceCurrent;
}