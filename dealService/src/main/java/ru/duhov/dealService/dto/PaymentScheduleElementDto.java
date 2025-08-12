package ru.duhov.dealService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Presentation of payment schedule element")
public class PaymentScheduleElementDto {

    @NotNull
    @Schema(description = "Number of element", example = "1")
    private Integer number;

    @NotNull
    @Schema(description = "Date of element", example = "2023-01-01")
    private LocalDate date;

    @NotNull
    @Schema(description = "Total payment of element", example = "25000")
    private BigDecimal totalPayment;

    @NotNull
    @Schema(description = "Interest payment of element", example = "25000")
    private BigDecimal interestPayment;

    @NotNull
    @Schema(description = "Debt payment of element", example = "25000")
    private BigDecimal debtPayment;

    @NotNull
    @Schema(description = "Remaining debt of element", example = "25000")
    private BigDecimal remainingDebt;


}