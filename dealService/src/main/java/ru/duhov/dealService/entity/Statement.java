package ru.duhov.dealService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.dealService.dto.enums.ApplicationStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "statement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statement {

    @Id
    @SequenceGenerator(name = "statement_generator", sequenceName = "statement_sequence", allocationSize = 1)
    @GeneratedValue(generator = "statement_generator", strategy = GenerationType.SEQUENCE)
    private UUID id;

    @Column(name = "client_id_uuid", nullable = false)
    private UUID clientId;

    @Column(name = "credit_id_uuid")
    private UUID creditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "creation_date", nullable = false)
    private OffsetDateTime creationDate;

    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private String appliedOffer;  // jsonb

    @Column(name = "sign_date")
    private OffsetDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history", columnDefinition = "jsonb")
    private String statusHistory;
}