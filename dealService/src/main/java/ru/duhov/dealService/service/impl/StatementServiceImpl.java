package ru.duhov.dealService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.StatementStatusHistoryDto;
import ru.duhov.dealService.dto.enums.ApplicationStatus;
import ru.duhov.dealService.dto.enums.ChangeType;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;
import ru.duhov.dealService.repository.StatementRepository;
import ru.duhov.dealService.service.StatementService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;

    @Override
    public Statement createStatement(Client client) {
        log.info("Creating new statement for client: {}", client);
        Statement statement = Statement.builder()
                .client(client)
                .creationDate(OffsetDateTime.now())
                .statusHistory(createStatementStatusHistoryJsonb(ApplicationStatus.PREAPPROVAL, ChangeType.AUTOMATIC).toString())
                .status(ApplicationStatus.PREAPPROVAL)
                .build();
        statementRepository.save(statement);
        log.info("Statement created: {}", statement);
        return statement;
    }

    @Override
    public Statement getStatementById(UUID statementId) {
        return statementRepository.getReferenceById(statementId);
    }

    @Override
    public void updateStatement(Statement statement, LoanOfferDto request) {
        Statement statementToSave = Statement.builder()
                .build();
    }

    @Override
    public void updateStatusAndStatusHistory(Statement statement) {

    }

    private StatementStatusHistoryDto createStatementStatusHistoryJsonb(ApplicationStatus applicationStatus, ChangeType changeType) {
        log.debug("Creating new status history entry: status = {}, changeType = {}", applicationStatus, changeType);
        return StatementStatusHistoryDto.builder()
                .status(applicationStatus)
                .changeType(changeType)
                .time(LocalDate.now())
                .build();
    }
}
