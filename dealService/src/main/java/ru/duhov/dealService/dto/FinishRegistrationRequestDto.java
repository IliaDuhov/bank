package ru.duhov.dealService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.dealService.dto.enums.Gender;
import ru.duhov.dealService.dto.enums.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishRegistrationRequestDto {

    @NotNull
    @Schema(description = "gender of registered user")
    private Gender gender;

    @NotNull
    @Schema(description = "Marital status", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Dependent amount", example = "25000")
    private Integer dependentAmount;

    @NotNull
    @PastOrPresent
    @Schema(description = "Passport issue date", example = "2003-01-01")
    private LocalDate passportIssueDate;

    @NotBlank
    @Schema(description = "Passport issue branch", example = "MG Russia")
    private String passportIssueBranch;

    @NotNull
    @Valid
    @Schema(description = "Employment details")
    private EmploymentDto employmentDto;

    @NotNull
    @Schema(description = "Account number", example = "12345678901234567890")
    private String accountNumber;

}