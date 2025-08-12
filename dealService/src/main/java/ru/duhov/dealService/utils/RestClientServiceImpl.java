package ru.duhov.dealService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.duhov.dealService.dto.*;
import ru.duhov.dealService.entity.Client;
import ru.duhov.dealService.entity.Statement;

import java.util.List;

@Service
@Slf4j
public class RestClientServiceImpl implements RestClientService{

    private final RestClient restClient;

    public RestClientServiceImpl() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8080/calculator")
                .build();
    }

    public RestClientServiceImpl(RestClient clientService){
        this.restClient = clientService;
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
    public CreditDto calculateCredit(ScoringDataDto request) {
        return restClient.post()
                .uri("/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CreditDto.class);
    }

}
