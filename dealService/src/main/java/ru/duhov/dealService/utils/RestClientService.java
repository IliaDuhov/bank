package ru.duhov.dealService.utils;

import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.dto.ScoringDataDto;

import java.util.List;

public interface RestClientService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request);

    CreditDto calculateCredit(ScoringDataDto request);
}
