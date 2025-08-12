package ru.duhov.dealService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duhov.dealService.entity.Statement;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
