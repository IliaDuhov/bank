package ru.duhov.dealService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.dealService.dto.enums.ApplicationStatus;
import ru.duhov.dealService.dto.enums.ChangeType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatementStatusHistoryDto {

    private ApplicationStatus status;

    private LocalDate time;

    private ChangeType changeType;
}