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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private static final String CURRENCY_NOT_FOUND = "User with id %s doesn't follow currency with code %s";
    private static final String CURRENCY_ALREADY_EXISTS = "Currency with code %s already exists";

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
        int preparedSize = Optional.ofNullable(size)
                .filter(s -> s > 0)
                .orElse(10);
        int preparedPage = Optional.ofNullable(page)
                .filter(p -> p > 0)
                .orElse(1);
        Pageable pageable = Pageable.ofSize(preparedSize)
                .withPage(preparedPage - 1);

        return cryptoCurrencyMapper.mapToPaginatedDto(
                cryptoCurrencyRepository.findAllByCryptoCurrencySubscriberId(pageable, authorizedUser.getId())
        );
    }

    @Override
    @Transactional
    public void followCryptoCurrency(
            String currencyCode,
            Integer checkingPeriod,
            UserEntity authorizedUser) {
        boolean cryptoCurrencyAlreadyExists = cryptoCurrencyRepository.existsByCurrencyCodeAndCryptoCurrencySubscriberId(
                currencyCode.toUpperCase(), authorizedUser.getId()
        );

        if (cryptoCurrencyAlreadyExists) {
            throw new ResponseStatusException(CONFLICT, CURRENCY_ALREADY_EXISTS.formatted(currencyCode));
        }

        coinGeckoRequestService.retrieveCryptoInfoBy(currencyCode)
                .publishOn(Schedulers.boundedElastic())
                .map(cryptoInfoResponseDto -> {
                    LocalDateTime now = LocalDateTime.now();
                    CryptoCurrencyEntity savedCryptoCurrency = cryptoCurrencyRepository.saveAndFlush(
                            CryptoCurrencyEntity.builder()
                                    .currencyCode(currencyCode.toUpperCase())
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
                .findByCurrencyCodeAndCryptoCurrencySubscriberId(currencyCode.toUpperCase(), authorizedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, CURRENCY_NOT_FOUND.formatted(authorizedUser.getId(), currencyCode.toUpperCase()
                )));
        List<CurrencyCheckResultEntity> allChecksInPeriod = currencyCheckResultRepository
                .findAllByCryptoCurrencyIdAndCheckTimeBetween(
                        currentCryptoCurrency.getId(), startOfPeriod, endOfPeriod
                )
                .stream()
                .filter(currencyCheckResultEntity -> Objects.nonNull(currencyCheckResultEntity.getCurrencyPrice()))
                .toList();

        return CryptoCurrencyStatisticsDto.builder()
                .currencyCode(currencyCode.toUpperCase())
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
