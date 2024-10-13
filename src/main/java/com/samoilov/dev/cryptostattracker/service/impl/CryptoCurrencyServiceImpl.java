package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.CryptoCurrencyEntity;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import com.samoilov.dev.cryptostattracker.repository.CryptoCurrencyRepository;
import com.samoilov.dev.cryptostattracker.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private final Mapper<CryptoCurrencyEntity, CryptoCurrencyDto> cryptoCurrencyMapper;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;


    @Override
    public PaginatedResponseDto<CryptoCurrencyDto> retrieveAllFollowedCryptoCurrencies(
            Integer page,
            Integer size,
            UserEntity authorizedUser) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return cryptoCurrencyMapper.mapToPaginatedDto(
                cryptoCurrencyRepository.findAllByCryptoCurrencySubscriberId(pageable, authorizedUser.getId())
        );
    }

    @Override
    public void followCryptoCurrency(
            String currencyCode,
            Integer checkingPeriod,
            UserEntity authorizedUser) {

    }

    @Override
    public CryptoCurrencyStatisticsDto retrieveCryptoCurrencyStatisticsForPeriod(
            String currencyCode,
            LocalDateTime startOfPeriod,
            LocalDateTime endOfPeriod,
            UserEntity authorizedUser) {
        return null;
    }
}
