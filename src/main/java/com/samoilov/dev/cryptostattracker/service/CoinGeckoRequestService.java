package com.samoilov.dev.cryptostattracker.service;

import com.samoilov.dev.cryptostattracker.dto.CryptoInfoResponseDto;
import reactor.core.publisher.Mono;

public interface CoinGeckoRequestService {

    Mono<CryptoInfoResponseDto> retrieveCryptoInfoBy(String currencyCode);

}
