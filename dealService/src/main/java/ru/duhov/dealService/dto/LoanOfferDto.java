package ru.duhov.dealService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan offer presentation")
public class LoanOfferDto {

    @NotNull
    @Schema(description = "Statement identificator")
    private UUID statementId;

    @NotNull
    @Schema(description = "Requested amount", example = "25000")
    private BigDecimal requestedAmount;

    @NotNull
    @Schema(description = "Total amount", example = "25000")
    private BigDecimal totalAmount;

    @NotNull
    @Schema(description = "Term for loan", example = "8")
    private Integer term;

    @NotNull
    @Schema(description = "Monthly payment", example = "4700")
    private BigDecimal monthlyPayment;

    @NotNull
    @Schema(description = "Loan offer rate", example = "20")
    private BigDecimal rate;

    @NotNull
    @Schema(description = "Insurance enabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "List of schedule elements", example = "true")
    private Boolean isSalaryClient;
}
