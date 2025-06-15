package ru.duhov.calculator.service.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.EmploymentDto;
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;
import ru.duhov.calculator.dto.enums.Position;
import ru.duhov.calculator.exception.CreditRefusedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    ScoringDataDto scoringData = new ScoringDataDto();
    EmploymentDto employment = new EmploymentDto();
    CreditDto creditDto = new CreditDto();

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
    void validateUnemployedTest() {
        employment.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        scoringData.setEmployment(employment);
        CreditRefusedException exception = assertThrows(
                CreditRefusedException.class,
                () -> userValidator.validate(scoringData, creditDto)
        );
        assertEquals("Credit refused to unemployed employer", exception.getMessage());
    }

    @Test
    void validateSelfEmployedTest(){
        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        scoringData.setEmployment(employment);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateBusinessOwner(){
        employment.setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        scoringData.setEmployment(employment);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(16), creditDto.getRate());
    }

    @Test
    void validateManagerPosition(){
        employment.setPosition(Position.MANAGER);
        scoringData.setEmployment(employment);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateTopManagerPosition(){
        employment.setPosition(Position.TOP_MANAGER);
        scoringData.setEmployment(employment);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(16), creditDto.getRate());
    }

    @Test
    void validateAmount(){
        scoringData.setAmount(new BigDecimal(1500000));
        Exception exception = assertThrows(CreditRefusedException.class, () ->
                userValidator.validate(scoringData, creditDto));
        assertEquals("Loan amount must not be greater than 24 times the salary.", exception.getMessage());
    }

    @Test
    void validateMaritalStatusMarried(){
        scoringData.setAmount(new BigDecimal(100000));
        scoringData.setMaritalStatus(MaritalStatus.MARRIED);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateMaritalStatusDivorced(){
        scoringData.setMaritalStatus(MaritalStatus.DIVORCED);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(21), creditDto.getRate());
    }

    @Test
    void validateMinAge(){
        scoringData.setBirthdate(LocalDate.now().minusYears(200));
        Exception exception = assertThrows(CreditRefusedException.class, () -> userValidator.validate(scoringData, creditDto));
        assertEquals("Age must be between 20 and 65 years old.", exception.getMessage());
    }

    @Test
    void validateMaxAge(){
        scoringData.setBirthdate(LocalDate.now().plusYears(100));
        Exception exception = assertThrows(CreditRefusedException.class, () -> userValidator.validate(scoringData, creditDto));
        assertEquals("Age must be between 20 and 65 years old.", exception.getMessage());
    }

    @Test
    void validateMaleGender(){
        scoringData.setBirthdate(LocalDate.now().minusYears(21));
        scoringData.setGender(Gender.MALE);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateFemaleGender(){
        scoringData.setBirthdate(LocalDate.now().minusYears(21));
        scoringData.setGender(Gender.FEMALE);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateNonBinaryGender(){
        scoringData.setBirthdate(LocalDate.now().minusYears(21));
        scoringData.setGender(Gender.NON_BINARY);
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(24), creditDto.getRate());
    }

    @Test
    void validateMaleGenderAndAge() {
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(45));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(14), creditDto.getRate());
    }

    @Test
    void validateFemaleGenderAndAge() {
        scoringData.setGender(Gender.FEMALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(40));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(14), creditDto.getRate());
    }

    @Test
    void validateFemaleGenderAndLessThirty() {
        scoringData.setGender(Gender.FEMALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(30));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateFemaleGenderAndAgeMoreSixty() {
        scoringData.setGender(Gender.FEMALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(65));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateMaleGenderAndAgeLessThirty() {
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(25));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }

    @Test
    void validateMaleGenderAndAgeMoreFiftyFive() {
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(60));
        userValidator.validate(scoringData, creditDto);
        assertEquals(BigDecimal.valueOf(17), creditDto.getRate());
    }


    @Test
    void validateWorkExperienceTotal(){
        employment.setWorkExperienceTotal(17);
        Exception exception = assertThrows(CreditRefusedException.class, () -> userValidator.validate(scoringData, creditDto));
        assertEquals("Work experience in total must me more than 18 months.", exception.getMessage());
    }

    @Test
    void validateWorkExperienceCurrent(){
        employment.setWorkExperienceCurrent(1);
        Exception exception = assertThrows(CreditRefusedException.class, () -> userValidator.validate(scoringData, creditDto));
        assertEquals("Current work experience must me more than 3 months.", exception.getMessage());
    }

}