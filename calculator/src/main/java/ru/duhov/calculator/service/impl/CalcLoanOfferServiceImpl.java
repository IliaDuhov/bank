package ru.duhov.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.duhov.calculator.dto.LoanOfferDto;
import ru.duhov.calculator.dto.LoanStatementRequestDto;
import ru.duhov.calculator.service.CalcLoanOfferService;
import ru.duhov.calculator.service.LoanCalculatorComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalcLoanOfferServiceImpl implements CalcLoanOfferService {

    private final LoanCalculatorComponent loanCalculator;

    @Value("${base.rate}")
    private BigDecimal BASE_RATE;

    @Override
    public List<LoanOfferDto> calcLoanOffers(LoanStatementRequestDto loanStatementRequest) {
        log.debug("Calculating loan offers {}", loanStatementRequest);
        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(createLoanOffer(loanStatementRequest, false, false));
        offers.add(createLoanOffer(loanStatementRequest, false, true));
        offers.add(createLoanOffer(loanStatementRequest, true, false));
        offers.add(createLoanOffer(loanStatementRequest, true, true));
        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
        log.debug("Calculated list of loan offers {}", offers);
        return offers;
    }

    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequest, Boolean isInsuranceEnabled,
                                         Boolean isSalaryClient){
        log.debug("Creating loan offer: loanStatementRequest={}, isInsuranceEnabled={}, isSalaryClient={}",
                loanStatementRequest, isInsuranceEnabled, isSalaryClient);

        BigDecimal loanAmount = loanStatementRequest.getAmount();
        log.debug("Setting loanAmount: {}", loanAmount);

        BigDecimal rate = loanCalculator.adjustRate(BASE_RATE, isInsuranceEnabled, isSalaryClient);
        log.debug("Calculated rate: {}", rate);

        BigDecimal principal = loanCalculator.calculatePrincipal(loanAmount, isInsuranceEnabled);
        log.debug("Calculated principal: {}", principal);

        BigDecimal monthlyPayment = loanCalculator.calculateAnnuityMonthlyPayment(principal, rate, loanStatementRequest.getTerm());
        log.debug("Calculated monthly payment: {}", monthlyPayment);

        BigDecimal totalAmount = loanCalculator.calculateTotalAmount(monthlyPayment, loanStatementRequest.getTerm());
        log.debug("Calculated totalAmount: {}", totalAmount);

        LoanOfferDto loanOffer = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .term(loanStatementRequest.getTerm())
                .requestedAmount(loanAmount)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .totalAmount(totalAmount)
                .isSalaryClient(isSalaryClient)
                .isInsuranceEnabled(isInsuranceEnabled)
                .build();

        log.debug("Calculated loan offer: {}", loanOffer);
        return loanOffer;
    }
}
