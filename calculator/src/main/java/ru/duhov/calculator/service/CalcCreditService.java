package ru.duhov.calculator.service;


import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.LoanStatementRequestDto;
import ru.duhov.calculator.dto.ScoringDataDto;


public interface CalcCreditService {

    public CreditDto calcCredit(ScoringDataDto scoringData);
}
