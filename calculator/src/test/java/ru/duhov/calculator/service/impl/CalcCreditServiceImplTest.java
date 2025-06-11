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
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;
import ru.duhov.calculator.dto.enums.Position;
import ru.duhov.calculator.service.CalcCreditService;
import ru.duhov.calculator.service.LoanCalculatorComponent;
import ru.duhov.calculator.service.utils.UserValidator;

import java.math.BigDecimal;
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

    ScoringDataDto scoringData = new ScoringDataDto();
    EmploymentDto employment = new EmploymentDto();
    CreditDto creditDto = new CreditDto();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        creditDto = new CreditDto();
        creditDto.setAmount(new BigDecimal(100000));
        creditDto.setTerm(18);
        creditDto.setRate(BigDecimal.valueOf(20));
        creditDto.setMonthlyPayment(BigDecimal.valueOf(6435));
        creditDto.setPsk(BigDecimal.valueOf(7.37));
        creditDto.setIsInsuranceEnabled(true);
        creditDto.setIsSalaryClient(true);
        creditDto.setPaymentSchedule(List.of());
    }

    @Test
    void calcCredit_shouldReturnCorrectCreditDto() {
       //TODO test
    }


}