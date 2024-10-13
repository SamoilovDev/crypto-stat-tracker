package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoInfoResponseDto;
import com.samoilov.dev.cryptostattracker.service.CoinGeckoRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinGeckoRequestServiceImpl implements CoinGeckoRequestService {

    private static final String VS_CURRENCY_QUERY_PARAM = "vs_currency";
    private static final String GECKO_COINS_PATH = "/coins/markets";
    private static final String IDS_QUERY_PARAM = "ids";
    private static final String USD_VS_CURRENCY = "usd";

    private final WebClient coinGeckoWebClient;

    @Override
    public Mono<CryptoInfoResponseDto> retrieveCryptoInfoBy(String currencyCode) {
        return coinGeckoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GECKO_COINS_PATH)
                        .queryParam(VS_CURRENCY_QUERY_PARAM, USD_VS_CURRENCY)
                        .queryParam(IDS_QUERY_PARAM, currencyCode.toLowerCase())
                        .build())
                .retrieve()
                .bodyToFlux(CryptoInfoResponseDto.class)
                .collectList()
                .map(List::getFirst);
    }

}
