package com.samoilov.dev.cryptostattracker.service;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CryptoCurrencyService {

    PaginatedResponseDto<CryptoCurrencyDto> retrieveAllFollowedCryptoCurrencies(
            Integer page,
            Integer size,
            UserEntity authorizedUser);

    @Transactional
    void followCryptoCurrency(String currencyCode, Integer checkingPeriod, UserEntity authorizedUser);

    CryptoCurrencyStatisticsDto retrieveCryptoCurrencyStatisticsForPeriod(
            String currencyCode,
            LocalDateTime startOfPeriod,
            LocalDateTime endOfPeriod,
            UserEntity authorizedUser);

}
