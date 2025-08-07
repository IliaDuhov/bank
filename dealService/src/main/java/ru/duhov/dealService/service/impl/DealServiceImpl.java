package ru.duhov.dealService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.FinishRegistrationRequestDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.service.ClientService;
import ru.duhov.dealService.service.CreditService;
import ru.duhov.dealService.service.DealService;
import ru.duhov.dealService.service.StatementService;
import ru.duhov.dealService.utils.RestClientService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CreditService creditService;
    private final RestClientService restClientService;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request) {
        Client client = clientService.createClient(request);
        Statement statement = statementService.createStatement(client);
        return restClientService.getLoanOffers(request);
    }

    @Override
    public void selectLoanOffer(LoanOfferDto request) {

    }

    @Override
    public void calculateLoan(FinishRegistrationRequestDto request, String statementId) {

    }
}
