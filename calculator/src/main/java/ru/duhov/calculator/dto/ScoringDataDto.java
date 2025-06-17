package ru.duhov.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Presentation of scoring data")
public class ScoringDataDto {

    @NotNull
    @DecimalMin(value = "20000")
    @Schema(description = "Credit amount", example = "25000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    @Schema(description = "Term in months", example = "12")
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "First name of the client", example = "Coul")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "Last name of the client", example = "Rust")
    private String lastName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    @Schema(description = "Middle name of the client", example = "Jackson")
    private String middleName;

    @NotNull
    @Schema(description = "Gender of the client", example = "MALE")
    private Gender gender;

    @NotNull
    @Schema(description = "Birthdate of the client", example = "2003-01-01")
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]+")
    @Schema(description = "Passport series", example = "1234")
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]+")
    @Schema(description = "Passport number", example = "567890")
    private String passportNumber;

    @NotNull
    @PastOrPresent
    @Schema(description = "Passport issue date", example = "2003-01-01")
    private LocalDate passportIssueDate;

    @NotBlank
    @Schema(description = "Passport issue branch", example = "MG Russia")
    private String passportIssueBranch;

    @NotNull
    @Schema(description = "Marital status", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Number of dependents", example = "0")
    private Integer dependentAmount;

    @NotNull
    @Valid
    @Schema(description = "Employment details")
    private EmploymentDto employment;

    @NotNull
    @Schema(description = "Account number", example = "12345678901234567890")
    private String accountNumber;

    @NotNull
    @Schema(description = "Insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "List of schedule elements", example = "true")
    private Boolean isSalaryClient;
}