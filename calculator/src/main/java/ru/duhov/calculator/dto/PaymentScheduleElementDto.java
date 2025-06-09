package ru.duhov.calculator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class PaymentScheduleElementDto {

    @NotNull
    private Integer number;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal totalPayment;

    @NotNull
    private BigDecimal interestPayment;

    @NotNull
    private BigDecimal debtPayment;

    @NotNull
    private BigDecimal remainingDebt;


}