package ru.duhov.dealService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.duhov.dealService.dto.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {

    @Id
    @SequenceGenerator(name = "credit_generator", sequenceName = "credit_sequence", allocationSize = 1)
    @GeneratedValue(generator = "credit_generator", strategy = GenerationType.SEQUENCE)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private Integer term;

    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "psk", nullable = false)
    private BigDecimal psk;

    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private String paymentSchedule;   // хранится как jsonb

    @Column(name = "insurance_enabled", nullable = false)
    private Boolean insuranceEnabled;

    @Column(name = "salary_client", nullable = false)
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status", nullable = false)
    private CreditStatus creditStatus;
}
