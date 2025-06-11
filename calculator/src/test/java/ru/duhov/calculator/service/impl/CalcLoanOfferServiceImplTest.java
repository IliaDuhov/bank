package ru.duhov.calculator.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.duhov.calculator.dto.LoanOfferDto;
import ru.duhov.calculator.dto.LoanStatementRequestDto;
import ru.duhov.calculator.service.LoanCalculatorComponent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcLoanOfferServiceImplTest {

    @Mock
    private LoanCalculatorComponent loanCalculator;

    @InjectMocks
    private CalcLoanOfferServiceImpl calcLoanOfferService;

    @Test
    void calculateLoanOffersTest() {
        LoanStatementRequestDto request = new LoanStatementRequestDto(
                BigDecimal.valueOf(100000),
                18,
                "Coul", "Rust", "Jackson",
                "coul@example.com",
                LocalDate.now().minusYears(21),
                "1234", "567890"
        );

        when(loanCalculator.adjustRate(any(), anyBoolean(), anyBoolean()))
                .thenReturn(BigDecimal.valueOf(20));
        when(loanCalculator.calculatePrincipal(any(), anyBoolean()))
                .thenReturn(BigDecimal.valueOf(100000));
        when(loanCalculator.calculateAnnuityMonthlyPayment(any(), any(), anyInt()))
                .thenReturn(BigDecimal.valueOf(8600));
        when(loanCalculator.calculateTotalAmount(any(), anyInt()))
                .thenReturn(BigDecimal.valueOf(103200));

        List<LoanOfferDto> offers = calcLoanOfferService.calcLoanOffers(request);

        assertEquals(4, offers.size());

        verify(loanCalculator, times(4)).adjustRate(any(), anyBoolean(), anyBoolean());
        verify(loanCalculator, times(4)).calculatePrincipal(any(), anyBoolean());
        verify(loanCalculator, times(4)).calculateAnnuityMonthlyPayment(any(), any(), anyInt());
        verify(loanCalculator, times(4)).calculateTotalAmount(any(), anyInt());
    }

}