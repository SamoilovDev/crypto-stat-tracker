package com.samoilov.dev.cryptostattracker.mapper;

import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.base.BaseEntity;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public interface Mapper<E extends BaseEntity, D extends Serializable> {

    D mapToDto(E entity);

    E mapToEntity(D dto);

    PaginatedResponseDto<D> mapToPaginatedDto(Page<E> entityPage);


}
