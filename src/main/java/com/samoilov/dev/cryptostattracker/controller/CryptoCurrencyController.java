package com.samoilov.dev.cryptostattracker.controller;

import com.samoilov.dev.cryptostattracker.controller.api.CryptoCurrencyApi;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import com.samoilov.dev.cryptostattracker.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CryptoCurrencyController implements CryptoCurrencyApi {

    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public ResponseEntity<PaginatedResponseDto<CryptoCurrencyDto>> retrieveAllFollowedCryptoCurrencies(
            Integer page,
            Integer size,
            UserEntity authorizedUser) {
        return null;
    }

    @Override
    public ResponseEntity<Void> followCryptoCurrency(
            String currencyCode,
            Integer checkingPeriod,
            UserEntity authorizedUser) {
        return null;
    }

    @Override
    public ResponseEntity<CryptoCurrencyStatisticsDto> retrieveCryptoCurrencyStatisticsForPeriod(
            String currencyCode,
            LocalDateTime startOfPeriod,
            LocalDateTime endOfPeriod,
            UserEntity authorizedUser) {
        return null;
    }
}
