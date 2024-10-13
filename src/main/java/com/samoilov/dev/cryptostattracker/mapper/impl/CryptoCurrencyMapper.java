package com.samoilov.dev.cryptostattracker.mapper.impl;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.CryptoCurrencyEntity;
import com.samoilov.dev.cryptostattracker.entity.CurrencyCheckResultEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import com.samoilov.dev.cryptostattracker.repository.CurrencyCheckResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoCurrencyMapper implements Mapper<CryptoCurrencyEntity, CryptoCurrencyDto> {

    private final CurrencyCheckResultRepository currencyCheckResultRepository;

    @Override
    public CryptoCurrencyDto mapToDto(CryptoCurrencyEntity entity) {
        return CryptoCurrencyDto.builder()
                .currencyCode(entity.getCurrencyCode())
                .currencyName(entity.getCurrencyName())
                .currentPrice(currencyCheckResultRepository
                        .findLastCurrencyCheckResultEntityByCryptoCurrencyId(entity.getId())
                        .map(CurrencyCheckResultEntity::getCurrencyPrice)
                        .orElse(null))
                .lastUpdateTime(entity.getLastCheckTime())
                .checkingPeriod(entity.getCheckingPeriod())
                .build();
    }

    @Override
    public CryptoCurrencyEntity mapToEntity(CryptoCurrencyDto dto) {
        return CryptoCurrencyEntity.builder()
                .currencyCode(dto.getCurrencyCode())
                .currencyName(dto.getCurrencyName())
                .checkingPeriod(dto.getCheckingPeriod())
                .build();
    }

    @Override
    public PaginatedResponseDto<CryptoCurrencyDto> mapToPaginatedDto(Page<CryptoCurrencyEntity> entityPage) {
        return PaginatedResponseDto.<CryptoCurrencyDto>builder()
                .size(entityPage.getSize())
                .page(entityPage.getNumber())
                .total(entityPage.getTotalPages() * entityPage.getSize())
                .data(entityPage.getContent()
                        .stream()
                        .map(this::mapToDto)
                        .toList())
                .build();
    }

}
