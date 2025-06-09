package ru.duhov.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.PaymentScheduleElementDto;
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.service.CalcCreditService;
import ru.duhov.calculator.service.LoanCalculatorComponent;
import ru.duhov.calculator.service.utils.UserValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalcCreditServiceImpl implements CalcCreditService {

    @Value("${base.rate}")
    private BigDecimal BASE_RATE;
    private final UserValidator userValidator;
    private final LoanCalculatorComponent loanCalculator;

    @Override
    public CreditDto calcCredit(ScoringDataDto scoringData) {
        return createCredit(scoringData);
    }

    private CreditDto createCredit(ScoringDataDto scoringData) {

        CreditDto creditDto = CreditDto.builder()
                .amount(scoringData.getAmount())
                .rate(BASE_RATE)
                .term(scoringData.getTerm())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .build();

        userValidator.validate(scoringData, creditDto);

        BigDecimal resultRate = loanCalculator.adjustRate(creditDto.getRate(), creditDto.getIsInsuranceEnabled(), creditDto.getIsSalaryClient());
        BigDecimal principal = loanCalculator.calculatePrincipal(creditDto.getAmount(), creditDto.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = loanCalculator.calculateAnnuityMonthlyPayment(principal, resultRate, creditDto.getTerm());
        BigDecimal totalAmount = loanCalculator.calculateTotalAmount(monthlyPayment, creditDto.getTerm());

        creditDto.setAmount(principal);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(resultRate);
        creditDto.setPsk(totalAmount);
        creditDto.setPaymentSchedule(calculatePaymentSchedule(creditDto));
        return creditDto;
    }

    private List<PaymentScheduleElementDto> calculatePaymentSchedule(CreditDto creditDto) {

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        BigDecimal remainingDebt = creditDto.getAmount();
        BigDecimal totalPayment = creditDto.getMonthlyPayment();
        BigDecimal interestPayment;
        BigDecimal debtPayment;
        BigDecimal rate = creditDto.getRate();
        Integer term = creditDto.getTerm();

        for (int i = 1; i <= term; i++) {
            startDate = startDate.plusMonths(1);
            interestPayment = calculateInterestPayment(remainingDebt, rate);
            debtPayment = calculateDebtPayment(totalPayment, interestPayment);
            remainingDebt = calculateRemainingDebt(remainingDebt, debtPayment);

            if (i == term) {
                totalPayment = totalPayment.add(remainingDebt);
                remainingDebt = BigDecimal.valueOf(0);
            }

            paymentSchedule.add(
                    PaymentScheduleElementDto.builder()
                            .number(i)
                            .date(startDate)
                            .totalPayment(totalPayment.setScale(2, RoundingMode.HALF_UP))
                            .interestPayment(interestPayment.setScale(2, RoundingMode.HALF_UP))
                            .debtPayment(debtPayment.setScale(2, RoundingMode.HALF_UP))
                            .remainingDebt(remainingDebt.setScale(2, RoundingMode.HALF_UP))
                            .build()
            );
        }
        return paymentSchedule;
    }

    private BigDecimal calculateInterestPayment(BigDecimal remainingDebt, BigDecimal rate) {
        return remainingDebt.multiply(rate.divide(BigDecimal.valueOf(12 * 100), 7, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDebtPayment(BigDecimal totalPayment, BigDecimal interestPayment) {
        return totalPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateRemainingDebt(BigDecimal remainingDebt, BigDecimal debtPayment) {
        return remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);
    }
}
