package ru.duhov.dealService.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.duhov.dealService.dto.*;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.service.ClientService;
import ru.duhov.dealService.service.CreditService;
import ru.duhov.dealService.service.StatementService;
import ru.duhov.dealService.utils.RestClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private ClientService clientService;
    @Mock
    private StatementService statementService;
    @Mock
    private CreditService creditService;
    @Mock
    private RestClientService restClientService;

    @InjectMocks
    private DealServiceImpl dealService;

    private LoanStatementRequestDto loanRequest;
    private Client client;
    private Statement statement;
    private LoanOfferDto loanOffer;
    private FinishRegistrationRequestDto finishRequest;
    private CreditDto creditDto;

    UUID uuid;
    Client expectedClient;
    Statement expectedStatement;

    @BeforeEach
    void setUp() {

        loanRequest = LoanStatementRequestDto.builder()
                .email("test@test.com")
                .build();

        client = Client.builder()
                .email(loanRequest.getEmail())
                .build();

        statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .client(client)
                .build();

        loanOffer = LoanOfferDto.builder()
                .statementId(statement.getStatementId())
                .build();

        finishRequest = FinishRegistrationRequestDto.builder()
                .build();

        creditDto = CreditDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .build();

    }


    @Test
    void getLoanOffers_shouldReturnList() {
        List<LoanOfferDto> expectedOffers = List.of(loanOffer);

        when(clientService.createClient(loanRequest)).thenReturn(client);
        when(statementService.createStatement(client)).thenReturn(statement);
        when(restClientService.getLoanOffers(loanRequest)).thenReturn(expectedOffers);

        List<LoanOfferDto> actual = dealService.getLoanOffers(loanRequest);

        assertThat(actual).isEqualTo(expectedOffers);
        verify(clientService).createClient(loanRequest);
        verify(statementService).createStatement(client);
        verify(restClientService).getLoanOffers(loanRequest);
    }

    @Test
    void selectLoanOffer_shouldUpdateStatement() {
        when(statementService.getStatementById(loanOffer.getStatementId())).thenReturn(statement);

        dealService.selectLoanOffer(loanOffer);

        verify(statementService).getStatementById(loanOffer.getStatementId());
        verify(statementService).updateStatement(statement, loanOffer);
    }
}