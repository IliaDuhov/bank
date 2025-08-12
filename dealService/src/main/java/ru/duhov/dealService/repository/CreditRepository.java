package ru.duhov.dealService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duhov.dealService.entity.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
