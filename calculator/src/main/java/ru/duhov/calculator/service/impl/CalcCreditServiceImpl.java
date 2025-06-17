package ru.duhov.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CalcCreditServiceImpl implements CalcCreditService {

    @Value("${base.rate}")
    private BigDecimal BASE_RATE;
    private final UserValidator userValidator;
    private final LoanCalculatorComponent loanCalculator;

    @Override
    public CreditDto calcCredit(ScoringDataDto scoringData) {
        log.debug("Calculating credit {}", scoringData);
        CreditDto creditDto = createCredit(scoringData);
        log.debug("Calculated credit {}", creditDto);
        return creditDto;
    }

    private CreditDto createCredit(ScoringDataDto scoringData) {
        log.debug("Creating credit {}", scoringData);
        CreditDto creditDto = CreditDto.builder()
                .amount(scoringData.getAmount())
                .rate(BASE_RATE)
                .term(scoringData.getTerm())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .build();

        log.debug("Validating scoring data {}", scoringData);
        userValidator.validate(scoringData, creditDto);

        BigDecimal resultRate = loanCalculator.adjustRate(creditDto.getRate(), creditDto.getIsInsuranceEnabled(), creditDto.getIsSalaryClient());
        log.debug("Calculated rate: {}", resultRate);

        BigDecimal principal = loanCalculator.calculatePrincipal(creditDto.getAmount(), creditDto.getIsInsuranceEnabled());
        log.debug("Calculated principal: {}", principal);

        BigDecimal monthlyPayment = loanCalculator.calculateAnnuityMonthlyPayment(principal, resultRate, creditDto.getTerm());
        log.debug("Calculated monthly payment: {}", monthlyPayment);

        BigDecimal totalAmount = loanCalculator.calculateTotalAmount(monthlyPayment, creditDto.getTerm());
        log.debug("Calculated totalAmount: {}", totalAmount);

        creditDto.setAmount(principal);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(resultRate);
        creditDto.setPsk(calculatePSK(creditDto.getAmount(), totalAmount, creditDto.getTerm()));
        creditDto.setPaymentSchedule(calculatePaymentSchedule(creditDto));
        log.debug("Credit dto created {}", creditDto);
        return creditDto;
    }

    private List<PaymentScheduleElementDto> calculatePaymentSchedule(CreditDto creditDto) {
        log.debug("Calculating payment schedule for credit: {}", creditDto);
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

            log.debug("Month {}: interest={}, debt={}, remainingDebt={}",
                    i,
                    interestPayment.setScale(2, RoundingMode.HALF_EVEN),
                    debtPayment.setScale(2, RoundingMode.HALF_EVEN),
                    remainingDebt.setScale(2, RoundingMode.HALF_EVEN)
            );

            if (i == term) {
                totalPayment = totalPayment.add(remainingDebt);
                log.debug("Total payment by element: {}", totalPayment);
                remainingDebt = BigDecimal.valueOf(0);
            }

            PaymentScheduleElementDto element = PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(startDate)
                    .totalPayment(totalPayment.setScale(2, RoundingMode.HALF_EVEN))
                    .interestPayment(interestPayment.setScale(2, RoundingMode.HALF_EVEN))
                    .debtPayment(debtPayment.setScale(2, RoundingMode.HALF_EVEN))
                    .remainingDebt(remainingDebt.setScale(2, RoundingMode.HALF_EVEN))
                    .build();

            paymentSchedule.add(element);
            log.debug("Added schedule element: {}", element);
        }
        log.debug("Calculated payment schedule for credit: {}", paymentSchedule);
        return paymentSchedule;
    }

    private BigDecimal calculateInterestPayment(BigDecimal remainingDebt, BigDecimal rate) {
        log.debug("Calculating interest payment: remainingDebt={}, rate={}", remainingDebt, rate);
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), 7, RoundingMode.HALF_EVEN);
        BigDecimal interest = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN);
        log.debug("Calculated interest payment: interest={}", interest);
        return interest;
    }

    private BigDecimal calculateDebtPayment(BigDecimal totalPayment, BigDecimal interestPayment) {
        log.debug("Calculating debt payment: totalPayment={}, interestPayment={}", totalPayment, interestPayment);
        BigDecimal debtPayment = totalPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_EVEN);
        log.debug("Calculated debt payment: debtPayment={}", debtPayment);
        return debtPayment;
    }

    private BigDecimal calculateRemainingDebt(BigDecimal remainingDebt, BigDecimal debtPayment) {
        log.debug("Calculating remaining debt: remainingDebt={}, debtPayment={}", remainingDebt, debtPayment);
        BigDecimal newRemainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_EVEN);
        log.debug("Calculated remaining debt: remainingDebt={}", newRemainingDebt);
        return remainingDebt;
    }

    private BigDecimal calculatePSK(BigDecimal principal, BigDecimal monthlyPayment, int termMonths) {
        log.debug("Calculating PSK by: principal={}, monthlyPayment={}, termMonths={}", principal, monthlyPayment, termMonths);
        if (principal == null || monthlyPayment == null ||
                principal.compareTo(BigDecimal.ZERO) <= 0 || termMonths <= 0) {
            log.warn("Invalid input for PSK calculation");
            return BigDecimal.ZERO;
        }
        double low = 0.0;
        double high = 1.0;
        double eps = 1e-10;
        double guessRate = 0.0;

        int maxIterations = 100;
        for (int i = 0; i < maxIterations; i++) {
            guessRate = (low + high) / 2.0;

            double npv = -principal.doubleValue();
            for (int m = 1; m <= termMonths; m++) {
                npv += monthlyPayment.doubleValue() / Math.pow(1 + guessRate, m);
            }
            if (Math.abs(npv) < eps) {
                break;
            }
            if (npv > 0) {
                low = guessRate;
            } else {
                high = guessRate;
            }
        }
        double annualRate = (Math.pow(1 + guessRate, 12) - 1) * 100;
        BigDecimal psk = BigDecimal.valueOf(annualRate).setScale(2, RoundingMode.HALF_EVEN);

        log.debug("Calculated PSK: {}%", psk);
        return psk;
    }


    public void setBaseRate(BigDecimal baseRate){
        this.BASE_RATE = baseRate;
    }

}
