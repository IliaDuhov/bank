package ru.duhov.dealService.jsonb;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.duhov.dealService.dto.enums.EmploymentStatus;
import ru.duhov.dealService.dto.enums.Position;

import java.io.Serializable;
import java.math.BigDecimal;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment implements Serializable {
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    private String employerInn;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
