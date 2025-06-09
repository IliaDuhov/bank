package ru.duhov.calculator.dto;

import ru.duhov.calculator.dto.enums.Gender;
import ru.duhov.calculator.dto.enums.MaritalStatus;

import java.time.LocalDate;

//TODO is it necessary here?
public class FinishRegistrationRequestDto {

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private EmploymentDto employment;

    private String accountNumber;
}
