package ru.duhov.dealService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.*;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.service.ClientService;
import ru.duhov.dealService.service.CreditService;
import ru.duhov.dealService.service.DealService;
import ru.duhov.dealService.service.StatementService;
import ru.duhov.dealService.utils.RestClientService;

import java.util.List;
import java.util.UUID;

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
        log.debug("Get loan offers from request {}", request);
        Client client = clientService.createClient(request);
        Statement statement = statementService.createStatement(client);
        return restClientService.getLoanOffers(request);
    }

    @Override
    public void selectLoanOffer(LoanOfferDto request) {
        log.debug("Selecting loan offer: {}", request);
        Statement statement = statementService.getStatementById(request.getStatementId());
        statementService.updateStatement(statement, request);
        log.debug("Loan offer selected.");
    }

    @Override
    public void calculateLoan(FinishRegistrationRequestDto request, String statementId) {
        log.debug("Calculating loan for statementId: {} with request: {}", statementId, request);
        Statement statement = statementService.getStatementById(UUID.fromString(statementId));
        ScoringDataDto scoringData = makeScoringDataDto(statement, request);
        log.debug("Scoring data created: {}", scoringData);
        CreditDto credit = restClientService.calculateCredit(scoringData);
        log.debug("Credit data calculated: {}", credit);
        creditService.createCredit(credit);
        log.debug("Loan calculated and credit created successfully.");
    }

    private ScoringDataDto makeScoringDataDto(Statement statement, FinishRegistrationRequestDto request) {
        log.debug("Creating scoring data DTO.");
        Client client = statement.getClient();

        return ScoringDataDto.builder()
                .accountNumber(request.getAccountNumber())
                .amount(statement.getAppliedOffer().getTotalAmount())
                .birthdate(client.getBirthDate())
                .dependentAmount(request.getDependentAmount())
                .employment(request.getEmploymentDto())
                .firstName(client.getFirstName())
                .gender(request.getGender())
                .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(statement.getAppliedOffer().getIsSalaryClient())
                .lastName(client.getLastName())
                .maritalStatus(request.getMaritalStatus())
                .middleName(client.getMiddleName())
                .passportIssueBranch(request.getPassportIssueBranch())
                .passportIssueDate(request.getPassportIssueDate())
                .passportNumber(client.getPassport().getNumber())
                .passportSeries(client.getPassport().getSeries())
                .term(statement.getAppliedOffer().getTerm())
                .build();
    }
}
