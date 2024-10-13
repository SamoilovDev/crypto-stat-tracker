package com.samoilov.dev.cryptostattracker.scheduler;

import com.samoilov.dev.cryptostattracker.entity.CurrencyCheckResultEntity;
import com.samoilov.dev.cryptostattracker.repository.CryptoCurrencyRepository;
import com.samoilov.dev.cryptostattracker.repository.CurrencyCheckResultRepository;
import com.samoilov.dev.cryptostattracker.service.CoinGeckoRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CryptoCurrencyCheckScheduler {

    private final CurrencyCheckResultRepository currencyCheckResultRepository;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CoinGeckoRequestService coinGeckoRequestService;

    @Scheduled(cron = "0 * * * * *")
    public void checkCryptoCurrencies() {
        cryptoCurrencyRepository.findAllCurrenciesToCheck()
                .forEach(cryptoCurrency -> coinGeckoRequestService
                        .retrieveCryptoInfoBy(cryptoCurrency.getCurrencyCode())
                        .publishOn(Schedulers.boundedElastic())
                        .map(cryptoInfo -> currencyCheckResultRepository.save(CurrencyCheckResultEntity.builder()
                                .checkTime(LocalDateTime.now())
                                .currencyPrice(cryptoInfo.getCurrentPrice())
                                .cryptoCurrency(cryptoCurrency)
                                .build()))
                        .then()
                        .subscribe());
    }

}
