package ru.duhov.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.duhov.calculator.dto.CreditDto;
import ru.duhov.calculator.dto.LoanOfferDto;
import ru.duhov.calculator.dto.LoanStatementRequestDto;
import ru.duhov.calculator.dto.ScoringDataDto;
import ru.duhov.calculator.service.impl.CalcCreditServiceImpl;
import ru.duhov.calculator.service.impl.CalcLoanOfferServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/calculator")
//TODO refactor swagger
public class CalculatorController {

    private final CalcLoanOfferServiceImpl calcLoanOfferService;
    private final CalcCreditServiceImpl calcCreditService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> offers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto){
        log.info("Received loan offers request: {}", loanStatementRequestDto);
        return new ResponseEntity<>(calcLoanOfferService.calcLoanOffers(loanStatementRequestDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreditDto> calc(@Valid @RequestBody ScoringDataDto scoringDataDto){
        log.info("Received credit creation request: {}", scoringDataDto);
        return new ResponseEntity<>(calcCreditService.calcCredit(scoringDataDto), HttpStatus.OK);
    }

}
