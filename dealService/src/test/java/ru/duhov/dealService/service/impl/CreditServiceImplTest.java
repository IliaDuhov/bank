package ru.duhov.dealService.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.enums.CreditStatus;
import ru.duhov.dealService.entity.Credit;
import ru.duhov.dealService.repository.CreditRepository;
import ru.duhov.dealService.service.CreditService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditServiceImpl creditServiceImpl;

    CreditDto request;
    Credit expectedCredit;

    @BeforeEach
    void setUp(){
        request = CreditDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .build();

        expectedCredit = Credit.builder()
                .amount(request.getAmount())
                .build();
    }

    @Test
    void createCredit_shouldMapDtoAndSaveCredit(){
        ArgumentCaptor<Credit> creditCaptor = ArgumentCaptor.forClass(Credit.class);
        when(creditRepository.save(any(Credit.class))).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0));

        Credit actualCredit = creditServiceImpl.createCredit(request);
        verify(creditRepository).save(creditCaptor.capture());

        Credit savedCredit = creditCaptor.getValue();

        assertThat(savedCredit.getAmount()).isEqualTo(request.getAmount());
        assertThat(savedCredit.getTerm()).isEqualTo(request.getTerm());
        assertThat(savedCredit.getMonthlyPayment()).isEqualTo(request.getMonthlyPayment());
        assertThat(savedCredit.getRate()).isEqualTo(request.getMonthlyPayment());
        assertThat(savedCredit.getRate()).isEqualTo(request.getRate());
        assertThat(savedCredit.getPsk()).isEqualTo(request.getPsk());
        assertThat(savedCredit.getInsuranceEnabled()).isEqualTo(request.getIsInsuranceEnabled());
        assertThat(savedCredit.getSalaryClient()).isEqualTo(request.getIsSalaryClient());

        assertThat(actualCredit).isEqualTo(savedCredit);
    }
}