package ru.duhov.calculator.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanOfferDto {

    private UUID statementId;

    private BigDecimal requestedAmount;

    private BigDecimal totalAmount;

    private Integer trem;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}