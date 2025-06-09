package ru.duhov.calculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.duhov.calculator.service.impl.CalcLoanOfferServiceImpl;

@RestController
@RequiredArgsConstructor
public class CalculatorController {

    private final CalcLoanOfferServiceImpl calcLoanOfferService;


}
