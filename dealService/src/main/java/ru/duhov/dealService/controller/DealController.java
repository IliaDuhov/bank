package ru.duhov.dealService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.service.DealService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/deal")
@Tag(name = "Deal service")
public class DealController {
    private final DealService dealService;

    @Operation(
            summary = "Calculating loan offers",
            description = "Return different examples of loan offers according to LoanStatementRequestDto"
    )
    @GetMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> dealStatementPost(LoanStatementRequestDto request) {
        log.info("Received request for loan offers: {}", request);
        List<LoanOfferDto> offers = dealService.getLoanOffers(request);
        log.info("Returning loan offers: {}", offers);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
