package ru.duhov.calculator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.EmploymentDto;
import ru.duhov.calculator.dto.PaymentScheduleElementDto;
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;
import ru.duhov.calculator.dto.enums.Position;
import ru.duhov.calculator.service.LoanCalculatorComponent;
import ru.duhov.calculator.service.utils.UserValidator;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalcCreditServiceImplTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private LoanCalculatorComponent loanCalculator;

    @InjectMocks
    private CalcCreditServiceImpl calcCreditService;

    private ScoringDataDto scoringData;
    private EmploymentDto employment;

    @BeforeEach
    void setUp(){

        calcCreditService.setBaseRate(new BigDecimal("20"));

        scoringData = new ScoringDataDto();
        employment = new EmploymentDto();

        scoringData.setAmount(new BigDecimal("100000"));
        scoringData.setTerm(12);
        scoringData.setFirstName("Coul");
        scoringData.setLastName("Rust");
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(30));
        scoringData.setMaritalStatus(MaritalStatus.MARRIED);
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(true);

        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employment.setSalary(new BigDecimal("50000"));
        employment.setPosition(Position.MANAGER);
        employment.setWorkExperienceTotal(24);
        employment.setWorkExperienceCurrent(12);
        scoringData.setEmployment(employment);
    }

    @Test
    void testCalcCredit_Success() {
        when(loanCalculator.adjustRate(any(), anyBoolean(), anyBoolean()))
                .thenReturn(new BigDecimal("13.00"));

        when(loanCalculator.calculatePrincipal(any(), anyBoolean()))
                .thenReturn(new BigDecimal("105000.00"));

        when(loanCalculator.calculateAnnuityMonthlyPayment(any(), any(), anyInt()))
                .thenReturn(new BigDecimal("8884.88"));

        when(loanCalculator.calculateTotalAmount(any(), anyInt()))
                .thenReturn(new BigDecimal("106618.56"));

        CreditDto creditDto = calcCreditService.calcCredit(scoringData);
        assertNotNull(creditDto);
        assertEquals(new BigDecimal("105000.00"), creditDto.getAmount());
        assertEquals(new BigDecimal("13.00"), creditDto.getRate());
        assertEquals(12, creditDto.getTerm());
        assertTrue(creditDto.getIsInsuranceEnabled());
        assertTrue(creditDto.getIsSalaryClient());
        assertEquals(new BigDecimal("8884.88"), creditDto.getMonthlyPayment());

        List<PaymentScheduleElementDto> schedule = creditDto.getPaymentSchedule();
        assertNotNull(schedule);
        assertEquals(12, schedule.size());
        assertEquals(1, schedule.get(0).getNumber());

        BigDecimal totalPaid = schedule.stream()
                .map(PaymentScheduleElementDto::getTotalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("106618.56"), totalPaid.subtract(creditDto.getAmount()));
        BigDecimal expectedPsk = totalPaid.subtract(new BigDecimal("105000.00"))
                .setScale(2, RoundingMode.HALF_UP);

    }

    @Test
    void testCalcCredit_ZeroTerm_ShouldReturnZeroPSK() {
        scoringData.setTerm(0);
        CreditDto creditDto = calcCreditService.calcCredit(scoringData);

        assertNotNull(creditDto);
        assertEquals(BigDecimal.ZERO, creditDto.getPsk());
    }

    @Test
    void testCalcCredit_ZeroAmount_ShouldReturnZeroPSK() {
        scoringData.setAmount(BigDecimal.ZERO);

        when(loanCalculator.adjustRate(any(), anyBoolean(), anyBoolean()))
                .thenReturn(BigDecimal.ZERO);
        when(loanCalculator.calculatePrincipal(any(), anyBoolean()))
                .thenReturn(BigDecimal.ZERO);
        when(loanCalculator.calculateAnnuityMonthlyPayment(any(), any(), anyInt()))
                .thenReturn(BigDecimal.ZERO);
        when(loanCalculator.calculateTotalAmount(any(), anyInt()))
                .thenReturn(BigDecimal.ZERO);

        CreditDto creditDto = calcCreditService.calcCredit(scoringData);

        assertNotNull(creditDto);
        assertEquals(BigDecimal.ZERO, creditDto.getPsk());
    }

    @Test
    void testCalcCredit_InvalidScoringData_ShouldThrowException() {
        doThrow(new IllegalArgumentException("Invalid data"))
                .when(userValidator).validate(any(), any());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calcCreditService.calcCredit(scoringData)
        );

        assertEquals("Invalid data", exception.getMessage());
    }
}