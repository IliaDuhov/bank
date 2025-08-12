package ru.duhov.dealService.service;

import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.entity.Credit;

public interface CreditService {
    public Credit createCredit(CreditDto creditDto);
}
