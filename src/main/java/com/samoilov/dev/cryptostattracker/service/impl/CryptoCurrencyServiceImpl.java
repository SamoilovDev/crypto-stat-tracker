package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.CurrencyCheckResultDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.CryptoCurrencyEntity;
import com.samoilov.dev.cryptostattracker.entity.CurrencyCheckResultEntity;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import com.samoilov.dev.cryptostattracker.repository.CryptoCurrencyRepository;
import com.samoilov.dev.cryptostattracker.repository.CurrencyCheckResultRepository;
import com.samoilov.dev.cryptostattracker.service.CoinGeckoRequestService;
import com.samoilov.dev.cryptostattracker.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private static final String CURRENCY_NOT_FOUND = "User with id %s doesn't follow currency with code %s";

    private final Mapper<CurrencyCheckResultEntity, CurrencyCheckResultDto> currencyCheckResultMapper;
    private final Mapper<CryptoCurrencyEntity, CryptoCurrencyDto> cryptoCurrencyMapper;
    private final CurrencyCheckResultRepository currencyCheckResultRepository;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CoinGeckoRequestService coinGeckoRequestService;


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
        coinGeckoRequestService.retrieveCryptoInfoBy(currencyCode)
                .publishOn(Schedulers.boundedElastic())
                .map(cryptoInfoResponseDto -> {
                    LocalDateTime now = LocalDateTime.now();
                    CryptoCurrencyEntity savedCryptoCurrency = cryptoCurrencyRepository.saveAndFlush(
                            CryptoCurrencyEntity.builder()
                                    .currencyCode(currencyCode)
                                    .currencyName(cryptoInfoResponseDto.getName())
                                    .checkingPeriod(checkingPeriod)
                                    .lastCheckTime(now)
                                    .cryptoCurrencySubscriber(authorizedUser)
                                    .build()
                    );
                    CurrencyCheckResultEntity currencyCheckResultEntity = CurrencyCheckResultEntity.builder()
                            .cryptoCurrency(savedCryptoCurrency)
                            .currencyPrice(cryptoInfoResponseDto.getCurrentPrice())
                            .checkTime(now)
                            .build();

                    return currencyCheckResultRepository.save(currencyCheckResultEntity);
                })
                .then()
                .block();
    }

    @Override
    public CryptoCurrencyStatisticsDto retrieveCryptoCurrencyStatisticsForPeriod(
            String currencyCode,
            LocalDateTime startOfPeriod,
            LocalDateTime endOfPeriod,
            UserEntity authorizedUser) {
        CryptoCurrencyEntity currentCryptoCurrency = cryptoCurrencyRepository
                .findByCurrencyCodeAndCryptoCurrencySubscriberId(currencyCode, authorizedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, CURRENCY_NOT_FOUND.formatted(authorizedUser.getId(), currencyCode
                )));
        List<CurrencyCheckResultEntity> allChecksInPeriod = currencyCheckResultRepository
                .findAllByCryptoCurrencyIdAndCheckTimeBetween(
                        currentCryptoCurrency.getId(), startOfPeriod, endOfPeriod
                )
                .stream()
                .filter(currencyCheckResultEntity -> Objects.nonNull(currencyCheckResultEntity.getCurrencyPrice()))
                .toList();

        return CryptoCurrencyStatisticsDto.builder()
                .currencyCode(currencyCode)
                .currencyName(currentCryptoCurrency.getCurrencyName())
                .maxPriceCheck(allChecksInPeriod.stream()
                        .max(Comparator.comparingDouble(CurrencyCheckResultEntity::getCurrencyPrice))
                        .map(currencyCheckResultMapper::mapToDto)
                        .orElse(null))
                .minPriceCheck(allChecksInPeriod.stream()
                        .min(Comparator.comparingDouble(CurrencyCheckResultEntity::getCurrencyPrice))
                        .map(currencyCheckResultMapper::mapToDto)
                        .orElse(null))
                .avgPrice(allChecksInPeriod.stream()
                        .mapToDouble(CurrencyCheckResultEntity::getCurrencyPrice)
                        .average()
                        .orElse(0.0d))
                .build();
    }
}
