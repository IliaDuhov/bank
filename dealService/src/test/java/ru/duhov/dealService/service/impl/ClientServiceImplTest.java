package ru.duhov.dealService.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.repository.ClientRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private LoanStatementRequestDto request;

    @BeforeEach
    void setUp() {
        request = LoanStatementRequestDto.builder()
                .firstName("Coul")
                .lastName("Rust")
                .middleName("Middle")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("coul.rust@example.com")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
    }

    @Test
    void createClient_shouldMapDtoAndSaveClient() {
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Client actualClient = clientService.createClient(request);

        verify(clientRepository).save(clientCaptor.capture());
        Client savedClient = clientCaptor.getValue();

        assertThat(savedClient.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(savedClient.getLastName()).isEqualTo(request.getLastName());
        assertThat(savedClient.getMiddleName()).isEqualTo(request.getMiddleName());
        assertThat(savedClient.getBirthDate()).isEqualTo(request.getBirthdate());
        assertThat(savedClient.getEmail()).isEqualTo(request.getEmail());

        assertThat(savedClient.getPassport()).isNotNull();
        assertThat(savedClient.getPassport().getSeries()).isEqualTo(request.getPassportSeries());
        assertThat(savedClient.getPassport().getNumber()).isEqualTo(request.getPassportNumber());

        assertThat(actualClient).isEqualTo(savedClient);
    }
}
