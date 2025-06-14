package ru.duhov.calculator.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.dto.enums.EmploymentStatus;
import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;
import ru.duhov.calculator.dto.enums.Position;
import ru.duhov.calculator.exception.CreditRefusedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Component
@Slf4j
public class UserValidator {

    public void validate(ScoringDataDto scoringData, CreditDto creditDto){
        log.debug("Validating scoring data {}", scoringData);
        checkEmploymentStatus(scoringData, creditDto);
        checkEmploymentPosition(scoringData, creditDto);
        checkAmount(scoringData);
        checkMaritalStatus(scoringData, creditDto);
        checkAge(scoringData);
        checkGender(scoringData, creditDto);
        checkWorkExperience(scoringData);
        log.debug("Validated scoring data for credit{}", creditDto);
    }

    private void checkEmploymentStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.debug("Validating employment status scoring data {}", scoringDataDto);
        EmploymentStatus status = scoringDataDto.getEmployment().getEmploymentStatus();

        if(status == EmploymentStatus.UNEMPLOYED){
            log.warn("Credit refused due to UNEMPLOYED status");
            throw new CreditRefusedException("Credit refused to unemployed employer");
        }
        if(status == EmploymentStatus.SELF_EMPLOYED){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(2)));
            log.debug("Calculated rate by 2 for SELF_EMPLOYED {}", creditDto);
        }
        if (status == EmploymentStatus.BUSINESS_OWNER){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(1)));
            log.debug("Calculated rate by 1 for BUSINESS_OWNER {}", creditDto);
        }
        log.debug("Validated EmploymentStatus for credit{}", creditDto);
    }

    private void checkEmploymentPosition(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.debug("Employment position validating scoring data, {}", scoringDataDto);
        Position position = scoringDataDto.getEmployment().getPosition();

        if(position == Position.MANAGER){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(2)));
            log.debug("Subtracted rate by 2 for MANAGER {}", creditDto);
        }
        if(position == Position.TOP_MANAGER){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
            log.debug("Subtracted rate by 3 for MANAGER {}", creditDto);
        }
        log.debug("Validated EmploymentPosition for credit{}", creditDto);
    }

    private void checkAmount(ScoringDataDto scoringDataDto){
        log.debug("Validating loan amount scoring data, {}", scoringDataDto);
        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(new BigDecimal("24"))) > 0) {
            log.warn("Credit refused due to amount: {}", scoringDataDto.getAmount());
            throw new CreditRefusedException("Loan amount must not be greater than 24 times the salary.");
        }
        log.debug("Validated loan amount for credit: {}", scoringDataDto);
    }

    private void checkMaritalStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.debug("Employment marital status validating {}", scoringDataDto);
        MaritalStatus maritalStatus = scoringDataDto.getMaritalStatus();

        if(maritalStatus == MaritalStatus.MARRIED){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
            log.debug("Subtracted rate by 3 for MARRIED {}", creditDto);
        }
        if(maritalStatus == MaritalStatus.DIVORCED){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(1)));
            log.debug("Calculated rate by 1 for MARRIED {}", creditDto);
        }
        log.debug("Validated MaritalStatus for credit{}", creditDto);
    }

    private void checkAge(ScoringDataDto scoringDataDto){
        log.debug("Validating age scoring data, {}", scoringDataDto);
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65) {
            log.warn("Credit refused due to age: {}", age);
            throw new CreditRefusedException("Age must be between 20 and 65 years old.");
        }
        log.debug("Validated age for credit{}", age);
    }

    private void checkGender(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.debug("Validating gender scoring data, {}", scoringDataDto);
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        Gender gender = scoringDataDto.getGender();
        if(gender == Gender.FEMALE && (age >= 32 && age <= 60)){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
            log.debug("Subtracted rate by 3 for FEMALE {}", creditDto);
        }
        if(gender == Gender.MALE && (age >= 30 && age <= 55)){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
            log.debug("Subtracted rate by 3 for MALE {}", creditDto);
        }
        if(gender == Gender.NON_BINARY){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(7)));
            log.debug("Calculated rate by 7 for NON_BINARY {}", creditDto);
        }
        log.debug("Validated Gender for credit{}", creditDto);
    }

    private void checkWorkExperience(ScoringDataDto scoringDataDto){
        log.debug("Validating work experience scoring data, {}", scoringDataDto);
        if(scoringDataDto.getEmployment().getWorkExperienceTotal() < 18 ){
            log.warn("Credit refused due to total work experience: {}",
                    scoringDataDto.getEmployment().getWorkExperienceTotal());
            throw new CreditRefusedException("Work experience in total must me more than 18 months.");
        }
        if(scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3){
            log.warn("Credit refused due to current work experience: {}",
                    scoringDataDto.getEmployment().getWorkExperienceCurrent());
            throw new CreditRefusedException("Current work experience must me more than 3 months.");
        }
        log.debug("Validated WorkExperience, {}", scoringDataDto);
    }
}
