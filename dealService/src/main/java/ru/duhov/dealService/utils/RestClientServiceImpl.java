package ru.duhov.dealService.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.dto.ScoringDataDto;

import java.util.List;

@Service
public class RestClientServiceImpl implements RestClientService{

    private final RestClient restClient;

    public RestClientServiceImpl() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8080/calculator")
                .build();
    }

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto request) {
        return restClient.post()
                .uri("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {});
    }

    @Override
    public CreditDto calculate(ScoringDataDto request) {
        return restClient.post()
                .uri("/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CreditDto.class);
    }
}
