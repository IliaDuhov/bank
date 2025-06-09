package ru.duhov.calculator.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LoanOfferDto {

    @NotNull
    private UUID statementId;

    @NotNull
    private BigDecimal requestedAmount;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private Integer trem;

    @NotNull
    private BigDecimal monthlyPayment;

    @NotNull
    private BigDecimal rate;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}