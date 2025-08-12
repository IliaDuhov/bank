package ru.duhov.dealService.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.LoanOfferDto;
import ru.duhov.dealService.dto.LoanStatementRequestDto;
import ru.duhov.dealService.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestClientServiceImplTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private RestClientServiceImpl restClientService;

    LoanStatementRequestDto loanRequest;
    ScoringDataDto scoringRequest;

    @BeforeEach
    void setUp() {
        loanRequest = LoanStatementRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        scoringRequest = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .build();
    }

    @Test
    void getLoanOffers_shouldReturnListOfOffers() {
        List<LoanOfferDto> expectedOffers = List.of(
                LoanOfferDto.builder().term(12).build(),
                LoanOfferDto.builder().term(24).build()
        );

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/offers")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(loanRequest)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expectedOffers);

        List<LoanOfferDto> actual = restClientService.getLoanOffers(loanRequest);

        assertThat(actual).isEqualTo(expectedOffers);
        verify(restClient).post();
        verify(requestBodyUriSpec).uri("/offers");
    }

    @Test
    void calculateCredit_shouldReturnCreditDto() {
        CreditDto expectedCredit = CreditDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .build();

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/calc")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(scoringRequest)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(CreditDto.class)).thenReturn(expectedCredit);

        CreditDto actual = restClientService.calculateCredit(scoringRequest);

        assertThat(actual).isEqualTo(expectedCredit);
        verify(restClient).post();
        verify(requestBodyUriSpec).uri("/calc");
    }
}
