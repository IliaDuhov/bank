package ru.duhov.dealService.service;

import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;

import java.util.UUID;

public interface StatementService {
    public Statement createStatement(Client client);

    public Statement getStatementById(UUID statementId);

    public void updateStatement(Statement statement, LoanOfferDto request);

    public void updateStatusAndStatusHistory(Statement statement);
}
