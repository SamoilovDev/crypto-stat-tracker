package com.samoilov.dev.cryptostattracker.repository;

import com.samoilov.dev.cryptostattracker.entity.CryptoCurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyEntity, Long> {

    Page<CryptoCurrencyEntity> findAllByCryptoCurrencySubscriberId(Pageable pageable, Long cryptoCurrencySubscriberId);

    Optional<CryptoCurrencyEntity> findByCurrencyCodeAndCryptoCurrencySubscriberId(
            String currencyCode,
            Long cryptoCurrencySubscriberId);

    boolean existsByCurrencyCodeAndCryptoCurrencySubscriberId(String currencyCode, Long cryptoCurrencySubscriberId);

    @Query(value = "SELECT * FROM crypto_currencies c " +
            "WHERE c.checking_period < EXTRACT(EPOCH FROM (NOW() - c.last_check_time)) / 60",
            nativeQuery = true)
    List<CryptoCurrencyEntity> findAllCurrenciesToCheck();

}