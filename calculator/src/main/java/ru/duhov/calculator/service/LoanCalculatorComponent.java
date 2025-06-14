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
        log.debug("Calculating annuity monthly payment: loanAmount={}, rate={}, term={}",
                loanAmount, rate, term);
        BigDecimal monthlyRate = rate.divide(new BigDecimal(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRatePowTerm = monthlyRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthlyRate.divide(onePlusRatePowTerm.subtract(BigDecimal.ONE),
                10, RoundingMode.HALF_UP).add(monthlyRate);

        BigDecimal monthlyPayment = loanAmount.multiply(annuityCoefficient);
        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_UP);
        log.debug("Calculated monthly payment: {}", monthlyPayment);
        return monthlyPayment;
    }

    public BigDecimal adjustRate(BigDecimal rate, boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.debug("Adjusting rate depending on settings: isInsuranceEnabled={},  isSalaryClient={}",
                isInsuranceEnabled, isSalaryClient);
        if (isInsuranceEnabled) {
            rate = rate.subtract(INSURANCE_DISCOUNT);
            log.debug("Calculating  rate by INSURANCE_DISCOUNT: rate={}", rate);
        }
        if (isSalaryClient) {
            rate = rate.subtract(SALARY_CLIENT_DISCOUNT);
            log.debug("Calculating  rate by SALARY_CLIENT_DISCOUNT: rate={}", rate);
        }
        log.debug("Calculated rate: rate={}", rate);
        return rate;
    }

    public BigDecimal calculatePrincipal(BigDecimal amount, boolean isInsuranceEnabled) {
        log.debug("Calculating principal: amount={}, isInsuranceEnabled={}", amount, isInsuranceEnabled);
        if(isInsuranceEnabled){
            amount = amount.add(INSURANCE_COST);
            log.debug("Increased amount: amount={}", amount);
            return amount;
        }
        log.debug("Calculated amount: amount={}", amount);
        return amount;
    }

    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, int term) {
        log.debug("Calculating total amount: monthlyPayment={}, term={}", monthlyPayment, term);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
        log.debug("Calculated total amount: totalAmount={}", totalAmount);
        return totalAmount;
    }
}
