package ru.duhov.calculator.service;

import ru.duhov.calculator.dto.LoanOfferDto;
import ru.duhov.calculator.dto.LoanStatementRequestDto;

import java.util.List;

public interface CalcLoanOfferService {

    public List<LoanOfferDto> calcLoanOffers(LoanStatementRequestDto loanStatementRequest);
}
