package ru.duhov.dealService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.duhov.dealService.dto.PaymentScheduleElementDto;
import ru.duhov.dealService.dto.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_id")
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Column(name = "insurance_enabled", nullable = false)
    private Boolean insuranceEnabled;

    @Column(name = "salary_client", nullable = false)
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status", nullable = false)
    private CreditStatus creditStatus;
}
