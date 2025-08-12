package ru.duhov.dealService.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.enums.ApplicationStatus;
import ru.duhov.dealService.dto.enums.ChangeType;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.jsonb.StatusHistory;
import ru.duhov.dealService.repository.ClientRepository;
import ru.duhov.dealService.repository.StatementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private StatementServiceImpl statementService;

    Statement expectedStatement;
    Client request;
    UUID statementId;

    @BeforeEach
    void setUp(){
        request = Client.builder()
                .email("coul.rust@example.com")
                .build();

        expectedStatement = Statement.builder()
                .statementId(statementId)
                .client(request)
                .creationDate(OffsetDateTime.now())
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(Collections.singletonList(
                        StatusHistory.builder()
                                .status(ApplicationStatus.PREAPPROVAL)
                                .changeType(ChangeType.AUTOMATIC)
                                .time(LocalDate.now())
                                .build()
                ))
                .build();
    }

    @Test
    void createStatement_shouldSaveClientAndStatement() {
        when(clientRepository.save(any(Client.class))).thenReturn(request);
        when(statementRepository.save(any(Statement.class))).thenReturn(expectedStatement);

        Statement created = statementService.createStatement(request);

        assertThat(created.getClient()).isEqualTo(request);
        assertThat(created.getStatus()).isEqualTo(ApplicationStatus.PREAPPROVAL);
        assertThat(created.getStatusHistory()).hasSize(1);
        assertThat(created.getStatusHistory().get(0).getStatus()).isEqualTo(ApplicationStatus.PREAPPROVAL);

        verify(clientRepository, times(1)).save(request);
        verify(statementRepository, times(1)).save(any(Statement.class));
    }

    @Test
    void getStatementById_whenFound_shouldReturnStatement() {
        when(statementRepository.findById(statementId)).thenReturn(Optional.of(expectedStatement));

        Statement actual = statementService.getStatementById(statementId);

        assertThat(actual).isEqualTo(expectedStatement);
        verify(statementRepository, times(1)).findById(statementId);
    }

    @Test
    void updateStatement(){
        LoanOfferDto offerDto = LoanOfferDto.builder()
                .term(24)
                .monthlyPayment(BigDecimal.valueOf(10000))
                .build();

        statementService.updateStatement(expectedStatement, offerDto);

        assertThat(expectedStatement.getStatus()).isEqualTo(ApplicationStatus.APPROVED);
        assertThat(expectedStatement.getAppliedOffer()).isEqualTo(offerDto);
        verify(statementRepository).save(expectedStatement);

    }
}