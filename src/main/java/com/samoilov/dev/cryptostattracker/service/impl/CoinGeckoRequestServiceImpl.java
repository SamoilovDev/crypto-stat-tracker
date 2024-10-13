package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoInfoResponseDto;
import com.samoilov.dev.cryptostattracker.service.CoinGeckoRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CoinGeckoRequestServiceImpl implements CoinGeckoRequestService {

    private final WebClient coinGeckoWebClient;

    @Override
    public Mono<CryptoInfoResponseDto> retrieveCryptoInfoBy(String currencyCode) {
        return null;
    }

}
