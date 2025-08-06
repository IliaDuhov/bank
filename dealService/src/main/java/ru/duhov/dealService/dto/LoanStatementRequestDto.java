package ru.duhov.dealService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Presentation of loan offer request")
public class LoanStatementRequestDto {

    @NotNull
    @DecimalMin(value = "20000")
    @Schema(description = "Requested amount", example = "25000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    @Schema(description = "Term for loan", example = "8")
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

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")
    @Schema(description = "Email of the client", example = "coul@example.com")
    private String email;

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
}
