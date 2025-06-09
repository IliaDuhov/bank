package ru.duhov.calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDto {

    @NotNull
    @DecimalMin(value = "20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String lastName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z-]+")
    private String middleName;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "[0-9]+")
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "[0-9]+")
    private String passportNumber;

    @NotNull
    @PastOrPresent
    private LocalDate passportIssueDate;

    @NotBlank
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    private Integer dependentAmount;

    @NotNull
    @Valid
    private EmploymentDto employment;

    @NotNull
    private String accountNumber;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;
}