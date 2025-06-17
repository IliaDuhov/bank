package ru.duhov.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Calculator")
public class CalculatorController {

    private final CalcLoanOfferServiceImpl calcLoanOfferService;
    private final CalcCreditServiceImpl calcCreditService;

    @Operation(
            summary = "Calculating loan offers",
            description = "Return different examples of loan offers according to LoanStatementRequestDto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan offers calculated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanOfferDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect request, validation exception",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Sever error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> offers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto){
        log.info("Received loan offers request: {}", loanStatementRequestDto);
        ResponseEntity<List<LoanOfferDto>> response = new ResponseEntity<>(calcLoanOfferService.calcLoanOffers(loanStatementRequestDto), HttpStatus.OK);
        log.info("Response for loan offers request {}", response);
        return response;
    }

    @Operation(
            summary = "Calculating credit",
            description = "Calculating a credit depending on scoring data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit calculated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreditDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect scoring data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error while calculating credit", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calc(@Valid @RequestBody ScoringDataDto scoringDataDto){
        log.info("Received credit creation request: {}", scoringDataDto);
        ResponseEntity<CreditDto> response = new ResponseEntity<>(calcCreditService.calcCredit(scoringDataDto), HttpStatus.OK);
        log.info("Response for credit creation request: {}", response);
        return response;
    }

}
