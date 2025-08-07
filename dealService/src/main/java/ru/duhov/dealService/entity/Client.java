package ru.duhov.dealService.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import ru.duhov.dealService.dto.enums.Gender;
import ru.duhov.dealService.dto.enums.MaritalStatus;
import ru.duhov.dealService.jsonb.Employment;
import ru.duhov.dealService.jsonb.Passport;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    @SequenceGenerator(name = "client_generator", sequenceName = "client_sequence", allocationSize = 1)
    @GeneratedValue(generator = "client_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "client_id")
    private UUID id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount", nullable = false)
    private Integer dependentAmount;

    @Column(name = "passport_id", nullable = false)
    private UUID passportId;

    @Column(name = "employment_id", nullable = false)
    private UUID employmentId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Employment employment;
}
