package ru.duhov.dealService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.jsonb.Employment;
import ru.duhov.dealService.jsonb.Passport;
import ru.duhov.dealService.repository.ClientRepository;
import ru.duhov.dealService.service.ClientService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client createClient(LoanStatementRequestDto request) {
        log.debug("Creating client based on LoanStatementRequestDto {}", request);
        Client client = Client.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .birthDate(request.getBirthdate())
                .email(request.getEmail())
                .passport(Passport.builder()
                        .series(request.getPassportSeries())
                        .number(request.getPassportNumber())
                        .build())
                .employment(Employment.builder()
                        .build())
                .build();
        log.debug("Created client {}", client);
        clientRepository.save(client);
        return client;
    }
}
