package ru.duhov.dealService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duhov.dealService.entity.Client;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
