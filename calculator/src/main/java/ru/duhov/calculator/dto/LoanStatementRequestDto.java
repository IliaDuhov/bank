package ru.duhov.calculator.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {

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

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")
    private String email;

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
}