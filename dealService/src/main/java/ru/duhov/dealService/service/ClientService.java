package ru.duhov.dealService.service;

import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.entity.Client;

public interface ClientService {
    public Client createClient(LoanStatementRequestDto loanStatementRequestDto );
}
