package ru.duhov.calculator.dto;

import ru.duhov.calculator.dto.enums.ChangeType;
import ru.duhov.calculator.dto.enums.Status;

import java.time.LocalDateTime;

//TODO is it necessary here?
public class StatementStatusHistoryDto {

    private Status status;

    private LocalDateTime time;

    private ChangeType changeType;
}
