package com.samoilov.dev.cryptostattracker.mapper.impl;

import com.samoilov.dev.cryptostattracker.dto.CurrencyCheckResultDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.CurrencyCheckResultEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CurrencyCheckResultMapper implements Mapper<CurrencyCheckResultEntity, CurrencyCheckResultDto> {

    @Override
    public CurrencyCheckResultDto mapToDto(CurrencyCheckResultEntity entity) {
        return CurrencyCheckResultDto.builder()
                .checkTime(entity.getCheckTime())
                .price(entity.getCurrencyPrice())
                .build();
    }

    @Override
    @Deprecated
    public CurrencyCheckResultEntity mapToEntity(CurrencyCheckResultDto dto) {
        return null;
    }

    @Override
    @Deprecated
    public PaginatedResponseDto<CurrencyCheckResultDto> mapToPaginatedDto(Page<CurrencyCheckResultEntity> entityPage) {
        return null;
    }

}
