package ru.duhov.calculator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanCalculatorComponentTest {

    @Value("${insurance.cost}")
    private BigDecimal INSURANCE_COST;
    @Value("${insurance.discount}")
    private BigDecimal INSURANCE_DISCOUNT;
    @Value("${salary.client.discount}")
    private BigDecimal SALARY_CLIENT_DISCOUNT;

    @Autowired
    private LoanCalculatorComponent loanCalculator;

    @Test
    void calculateMonthlyPaymentTest() {
        BigDecimal loanAmount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(20);
        int term = 18;
        BigDecimal monthlyPayment = loanCalculator.calculateAnnuityMonthlyPayment(loanAmount, rate, term);
        assertEquals(BigDecimal.valueOf(6476.32).setScale(2, RoundingMode.HALF_UP), monthlyPayment);
    }

    @Test
    void adjustRateTest() {
        BigDecimal rate = BigDecimal.valueOf(20);
        boolean isInsuranceEnabled = true;
        boolean isSalaryClient = true;
        BigDecimal adjustedRate = loanCalculator.adjustRate(rate, isInsuranceEnabled, isSalaryClient);
        assertEquals(rate.subtract(INSURANCE_DISCOUNT).subtract(SALARY_CLIENT_DISCOUNT), adjustedRate);
    }

    @Test
    void calculatePrincipalTest() {
        BigDecimal amount = BigDecimal.valueOf(100000);
        boolean isInsuranceEnabled = true;
        BigDecimal principal = loanCalculator.calculatePrincipal(amount, isInsuranceEnabled);
        assertEquals(BigDecimal.valueOf(100000).add(INSURANCE_COST), principal);
    }

    @Test
    void calculatePrincipalTestNoInsuarance() {
        BigDecimal amount = BigDecimal.valueOf(100000);
        boolean isInsuranceEnabled = false;
        BigDecimal principal = loanCalculator.calculatePrincipal(amount, isInsuranceEnabled);
        assertEquals(BigDecimal.valueOf(100000), principal);
    }



    @Test
    void calculateTotalAmountTest() {
        BigDecimal monthlyPayment = BigDecimal.valueOf(8600);
        int term = 12;
        BigDecimal totalAmount = loanCalculator.calculateTotalAmount(monthlyPayment, term);
        assertEquals(monthlyPayment.multiply(BigDecimal.valueOf(term)), totalAmount);
    }
}