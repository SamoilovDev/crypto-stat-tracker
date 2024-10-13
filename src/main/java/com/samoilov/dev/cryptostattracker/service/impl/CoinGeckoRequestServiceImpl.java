package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoInfoResponseDto;
import com.samoilov.dev.cryptostattracker.service.CoinGeckoRequestService;
import io.netty.handler.ssl.SslHandshakeTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinGeckoRequestServiceImpl implements CoinGeckoRequestService {


    private static final String RESPONSE_EXCEPTION_LOG_TEMPLATE = "\nException occurred: {}\nHttpStatus: {}\nResponse body: {}";
    private static final String REQUEST_EXCEPTION_LOG_TEMPLATE = "Exception during request to {} with message: {}";
    private static final String CURRENCY_NOT_FOUND = "Currency with %s code not found";

    private static final String VS_CURRENCY_QUERY_PARAM = "vs_currency";
    private static final String GECKO_COINS_PATH = "/coins/markets";
    private static final String USD_VS_CURRENCY = "usd";

    private final WebClient coinGeckoWebClient;

    @Override
    public Mono<CryptoInfoResponseDto> retrieveCryptoInfoBy(String currencyCode) {
        return coinGeckoWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(GECKO_COINS_PATH)
                        .queryParam(VS_CURRENCY_QUERY_PARAM, USD_VS_CURRENCY)
                        .build())
                .retrieve()
                .bodyToFlux(CryptoInfoResponseDto.class)
                .collectList()
                .transform(this::handleExceptions)
                .map(cryptoInfoResponseDtos -> cryptoInfoResponseDtos.stream()
                        .filter(cryptoInfoResponseDto -> Objects.equals(
                                cryptoInfoResponseDto.getSymbol(), currencyCode.toLowerCase()
                        ))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(
                                NOT_FOUND, CURRENCY_NOT_FOUND.formatted(currencyCode)
                        )));
    }

    private <T> Mono<T> handleExceptions(Mono<T> mono) {
        return mono
                .onErrorMap(WebClientResponseException.BadRequest.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.Unauthorized.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.Forbidden.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.NotFound.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.Conflict.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.GatewayTimeout.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.TooManyRequests.class, this::mapExceptionToResponseException)
                .onErrorMap(WebClientResponseException.class, this::mapExceptionToResponseException)
                .onErrorMap(SslHandshakeTimeoutException.class, e -> new ResponseStatusException(
                        INTERNAL_SERVER_ERROR, e.getMessage()
                ))
                .onErrorMap(WebClientRequestException.class, e -> {
                    log.error(REQUEST_EXCEPTION_LOG_TEMPLATE, e.getUri(), e.getMessage());
                    return new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
                });
    }

    private <E extends WebClientResponseException> ResponseStatusException mapExceptionToResponseException(E ex) {
        log.error(RESPONSE_EXCEPTION_LOG_TEMPLATE, ex.getMessage(), ex.getStatusCode(), ex.getResponseBodyAsString());
        return new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
    }

}
