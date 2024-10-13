package com.samoilov.dev.cryptostattracker.repository;

import com.samoilov.dev.cryptostattracker.entity.CurrencyCheckResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CurrencyCheckResultRepository extends JpaRepository<CurrencyCheckResultEntity, Long> {

    @Query("SELECT c FROM CurrencyCheckResultEntity c " +
            "WHERE c.cryptoCurrency.id = :cryptoCurrencyId " +
            "ORDER BY c.checkTime DESC")
    Optional<CurrencyCheckResultEntity> findLastCurrencyCheckResultEntityByCryptoCurrencyId(Long cryptoCurrencyId);

    List<CurrencyCheckResultEntity> findAllByCryptoCurrencyIdAndCheckTimeBetween(
            Long cryptoCurrencyId,
            LocalDateTime startOfPeriod,
            LocalDateTime endOfPeriod);

}