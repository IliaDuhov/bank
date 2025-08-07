package ru.duhov.dealService.jsonb;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import ru.duhov.dealService.dto.enums.ApplicationStatus;
import ru.duhov.dealService.dto.enums.ChangeType;

import java.io.Serializable;
import java.time.LocalDate;

public class StatusHistory implements Serializable {
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDate time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}
