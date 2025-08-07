package ru.duhov.dealService.service;

import ru.duhov.dealService.dto.FinishRegistrationRequestDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request);

    void selectLoanOffer(LoanOfferDto request);

    void calculateLoan(FinishRegistrationRequestDto request, String statementId);
}
