package ru.duhov.dealService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.enums.ApplicationStatus;
import ru.duhov.dealService.dto.enums.ChangeType;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.jsonb.StatusHistory;
import ru.duhov.dealService.repository.ClientRepository;
import ru.duhov.dealService.repository.StatementRepository;
import ru.duhov.dealService.service.StatementService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public Statement createStatement(Client client) {
        log.debug("Creating new statement for client: {}", client);
        client = clientRepository.save(client);
        Statement statement = Statement.builder()
                .client(client)
                .creationDate(OffsetDateTime.now())
                .statusHistory(Collections.singletonList(createStatusHistory(ApplicationStatus.PREAPPROVAL, ChangeType.AUTOMATIC)))
                .status(ApplicationStatus.PREAPPROVAL)
                .build();
        statementRepository.save(statement);
        log.debug("Statement created: {}", statement);
        return statement;
    }

    @Override
    public Statement getStatementById(UUID statementId) {
        Statement statement = statementRepository.findById(statementId).orElseThrow();
        log.debug("Find statement by {} by id {}", statement, statementId);
        return statement;
    }

    @Override
    public void updateStatement(Statement statement, LoanOfferDto request) {
        log.debug("Updating statement: {}", statement);
        StatusHistory statusHistoryJsonb = createStatusHistory(ApplicationStatus.APPROVED, ChangeType.AUTOMATIC);
        statement.setStatus(statusHistoryJsonb.getStatus());
        statement.setAppliedOffer(request);
        statementRepository.save(statement);
        log.debug("Statement updated.");
    }

    private StatusHistory createStatusHistory(ApplicationStatus applicationStatus, ChangeType changeType) {
        log.info("Creating new status history entry: status = {}, changeType = {}", applicationStatus, changeType);
        return StatusHistory.builder()
                .status(applicationStatus)
                .changeType(changeType)
                .time(LocalDate.now())
                .build();
    }
}
