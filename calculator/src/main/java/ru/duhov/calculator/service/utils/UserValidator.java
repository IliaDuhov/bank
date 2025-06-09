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
        checkEmploymentStatus(scoringData, creditDto);
        checkEmploymentPosition(scoringData, creditDto);
        checkAmount(scoringData);
        checkMaritalStatus(scoringData, creditDto);
        checkAge(scoringData);
        checkGender(scoringData, creditDto);
        checkWorkExperience(scoringData);
    }

    private void checkEmploymentStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        EmploymentStatus status = scoringDataDto.getEmployment().getEmploymentStatus();

        if(status == EmploymentStatus.UNEMPLOYED){
            throw new CreditRefusedException("Credit refused to unemployed employer");
        }
        if(status == EmploymentStatus.SELF_EMPLOYED){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(2)));
        }
        if (status == EmploymentStatus.BUSINESS_OWNER){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(1)));
        }
    }

    private void checkEmploymentPosition(ScoringDataDto scoringDataDto, CreditDto creditDto){
        Position position = scoringDataDto.getEmployment().getPosition();

        if(position == Position.MANAGER){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(2)));
        }
        if(position == Position.TOP_MANAGER){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
        }
    }

    private void checkAmount(ScoringDataDto scoringDataDto){
        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(new BigDecimal("24"))) > 0) {
            throw new CreditRefusedException("Loan amount must not be greater than 24 times the salary.");
        }
    }

    private void checkMaritalStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        MaritalStatus maritalStatus = scoringDataDto.getMaritalStatus();

        if(maritalStatus == MaritalStatus.MARRIED){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
        }
        if(maritalStatus == MaritalStatus.DIVORCED){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(3)));
        }
    }

    private void checkAge(ScoringDataDto scoringDataDto){
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65) {
            throw new CreditRefusedException("Age must be between 20 and 65 years old.");
        }
    }

    private void checkGender(ScoringDataDto scoringDataDto, CreditDto creditDto){
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        Gender gender = scoringDataDto.getGender();
        if(gender == Gender.FEMALE && (age >= 32 && age <= 60)){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
        }
        if(gender == Gender.MALE && (age >= 30 && age <= 55)){
            creditDto.setRate(creditDto.getRate().subtract(BigDecimal.valueOf(3)));
        }
        if(gender == Gender.NON_BINARY){
            creditDto.setRate(creditDto.getRate().add(BigDecimal.valueOf(7)));
        }
    }

    private void checkWorkExperience(ScoringDataDto scoringDataDto){
        if(scoringDataDto.getEmployment().getWorkExperienceTotal() < 18 ){
            throw new CreditRefusedException("Work experience in total must me more than 18 months");
        }
        if(scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3){
            throw new CreditRefusedException("Current work experience must me more than 3 months");
        }
    }
}
