package ru.duhov.calculator.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class LoanCalculatorComponent {

    @Value("${insurance.cost}")
    private BigDecimal INSURANCE_COST;
    @Value("${insurance.discount}")
    private BigDecimal INSURANCE_DISCOUNT;
    @Value("${salary.client.discount}")
    private BigDecimal SALARY_CLIENT_DISCOUNT;

    public BigDecimal calculateAnnuityMonthlyPayment(BigDecimal loanAmount, BigDecimal rate, int term) {
        BigDecimal monthlyRate = rate.divide(new BigDecimal(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRatePowTerm = monthlyRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthlyRate.divide(onePlusRatePowTerm.subtract(BigDecimal.ONE),
                10, RoundingMode.HALF_UP).add(monthlyRate);

        BigDecimal monthlyPayment = loanAmount.multiply(annuityCoefficient);
        return monthlyPayment.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal adjustRate(BigDecimal rate, boolean isInsuranceEnabled, boolean isSalaryClient) {
        if (isInsuranceEnabled) {
            rate = rate.subtract(INSURANCE_DISCOUNT);
        }
        if (isSalaryClient) {
            rate = rate.subtract(SALARY_CLIENT_DISCOUNT);
        }
        return rate;
    }

    public BigDecimal calculatePrincipal(BigDecimal amount, boolean isInsuranceEnabled) {
        if(isInsuranceEnabled){
            amount.add(INSURANCE_COST);
        }
        return amount;
    }

    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, int term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }
}
