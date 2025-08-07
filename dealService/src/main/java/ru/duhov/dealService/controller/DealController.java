package ru.duhov.dealService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.FinishRegistrationRequestDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/deal")
@Tag(name = "Deal service")
public class DealController {

    private final DealService dealService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting list of loan offers",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanOfferDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect request, validation exception",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Sever error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> dealStatementPost(@RequestBody LoanStatementRequestDto request) {
        log.info("Received request for loan offers: {}", request);
        List<LoanOfferDto> offers = dealService.getLoanOffers(request);
        log.info("Returning loan offers: {}", offers);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Select loan offer",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Incorrect scoring data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error while calculating credit", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/offer/select")
    public ResponseEntity<Void> dealOfferSelectPost(@RequestBody LoanOfferDto request) {
        log.info("Received request to select loan offer: {}", request);
        dealService.selectLoanOffer(request);
        log.info("Loan offer selected successfully.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculate loan by id",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Incorrect scoring data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error while calculating credit", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> dealCalculateStatementIdPost(@PathVariable String statementId, @RequestBody FinishRegistrationRequestDto request) {
        log.info("Received request to calculate loan for statementId: {} with request: {}", statementId, request);
        dealService.calculateLoan(request, statementId);
        log.info("Loan calculated successfully.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
