package com.samoilov.dev.cryptostattracker.repository;

import com.samoilov.dev.cryptostattracker.entity.CryptoCurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyEntity, Long> {

    Page<CryptoCurrencyEntity> findAllByCryptoCurrencySubscriberId(Pageable pageable, Long cryptoCurrencySubscriberId);

}