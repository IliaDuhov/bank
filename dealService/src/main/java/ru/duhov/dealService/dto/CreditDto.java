package ru.duhov.dealService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credit presentation")
public class CreditDto {

    @NotNull
    @DecimalMin(value = "20000")
    @Schema(description = "Credit amount", example = "25000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    @Schema(description = "Term in months", example = "12")
    private Integer term;

    @NotNull
    @Schema(description = "Monthly payment", example = "4700")
    private BigDecimal monthlyPayment;

    @NotNull
    @Schema(description = "Credit rate", example = "20")
    private BigDecimal rate;

    @NotNull
    @Schema(description = "Full value of credit", example = "7")
    private BigDecimal psk;

    @NotNull
    @Schema(description = "Insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Salary client", example = "true")
    private Boolean isSalaryClient;

    @NotNull
    @Valid
    @Schema(description = "List of schedule elements")
    private List<PaymentScheduleElementDto> paymentSchedule;
}

