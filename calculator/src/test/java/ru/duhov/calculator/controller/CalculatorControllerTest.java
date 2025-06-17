package ru.duhov.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.duhov.calculator.dto.*;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;
import ru.duhov.calculator.dto.enums.Position;
import ru.duhov.calculator.service.CalcCreditService;
import ru.duhov.calculator.service.CalcLoanOfferService;
import ru.duhov.calculator.service.impl.CalcCreditServiceImpl;
import ru.duhov.calculator.service.impl.CalcLoanOfferServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private CalcCreditServiceImpl calcCreditService;
    @Mock
    private CalcLoanOfferServiceImpl calcLoanOfferService;
    @InjectMocks
    private CalculatorController calculatorController;

    LoanStatementRequestDto loanStatement = new LoanStatementRequestDto();
    LoanOfferDto loanOfferDto = new LoanOfferDto();

    ScoringDataDto scoringData = new ScoringDataDto();
    EmploymentDto employment = new EmploymentDto();

    @BeforeEach
    void setUp() {
        scoringData.setAmount(new BigDecimal(100000));
        scoringData.setTerm(18);
        scoringData.setFirstName("Coul");
        scoringData.setLastName("Rust");
        scoringData.setMiddleName("Jackson");
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(21));
        scoringData.setPassportSeries("1234");
        scoringData.setPassportNumber("123456");
        scoringData.setPassportIssueDate(LocalDate.now().minusMonths(1));
        scoringData.setPassportIssueBranch("ГУ МВД по Саратовской области");
        scoringData.setMaritalStatus(MaritalStatus.MARRIED);
        scoringData.setDependentAmount(0);

        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employment.setEmployerINN("1234567890");
        employment.setSalary(new BigDecimal(50000));
        employment.setPosition(Position.MANAGER);
        employment.setWorkExperienceTotal(100);
        employment.setWorkExperienceCurrent(20);

        scoringData.setEmployment(employment);
        scoringData.setAccountNumber("1234567890");
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(true);

        loanStatement.setAmount(new BigDecimal("100000"));
        loanStatement.setTerm(18);
        loanStatement.setFirstName("Coul");
        loanStatement.setLastName("Rust");
        loanStatement.setMiddleName("Jackson");
        loanStatement.setEmail("coul@example.com");
        loanStatement.setBirthdate(LocalDate.now().minusYears(21));
        loanStatement.setPassportSeries("1234");
        loanStatement.setPassportNumber("123456");
    }

    @Test
    void offersTest() {
        LoanOfferDto o1 = LoanOfferDto.builder()
                .statementId(java.util.UUID.randomUUID())
                .requestedAmount(BigDecimal.valueOf(100_000))
                .totalAmount(BigDecimal.valueOf(108_000))
                .term(20)
                .monthlyPayment(BigDecimal.valueOf(6000))
                .rate(BigDecimal.valueOf(20))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        LoanOfferDto o2 = LoanOfferDto.builder()
                .statementId(java.util.UUID.randomUUID())
                .requestedAmount(BigDecimal.valueOf(100_000))
                .totalAmount(BigDecimal.valueOf(105_000))
                .term(20)
                .monthlyPayment(BigDecimal.valueOf(5833.33))
                .rate(BigDecimal.valueOf(20))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
        when(calcLoanOfferService.calcLoanOffers(loanStatement)).thenReturn(List.of(o1, o2));

        ResponseEntity<List<LoanOfferDto>> resp = calculatorController.offers(loanStatement);

        assertEquals(200, resp.getStatusCodeValue());
        List<LoanOfferDto> body = resp.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals(o1.getRate(), body.get(0).getRate());
        assertTrue(body.get(1).getIsInsuranceEnabled());

        verify(calcLoanOfferService, times(1)).calcLoanOffers(loanStatement);
    }

    @Test
    void calcTest() {
        CreditDto result = CreditDto.builder()
                .amount(scoringData.getAmount())
                .rate(BigDecimal.valueOf(20))
                .term(scoringData.getTerm())
                .monthlyPayment(BigDecimal.valueOf(7500))
                .psk(BigDecimal.valueOf(9.0))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .paymentSchedule(List.of())
                .build();
        when(calcCreditService.calcCredit(scoringData)).thenReturn(result);

        ResponseEntity<CreditDto> resp = calculatorController.calc(scoringData);

        assertEquals(200, resp.getStatusCodeValue());
        CreditDto body = resp.getBody();
        assertNotNull(body);
        assertEquals(result.getAmount(), body.getAmount());
        assertEquals(result.getRate(), body.getRate());
        assertEquals(result.getMonthlyPayment(), body.getMonthlyPayment());
        assertEquals(result.getPsk(), body.getPsk());
        assertEquals(scoringData.getIsInsuranceEnabled(), body.getIsInsuranceEnabled());
        assertEquals(scoringData.getIsSalaryClient(), body.getIsSalaryClient());
        verify(calcCreditService, times(1)).calcCredit(scoringData);
    }
}