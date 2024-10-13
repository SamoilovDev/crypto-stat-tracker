package com.samoilov.dev.cryptostattracker.mapper.impl;

import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.dto.UserDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserEntity, UserDto> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto mapToDto(UserEntity entity) {
        return UserDto.builder()
                .username(entity.getUsername())
                .build();
    }

    @Override
    public UserEntity mapToEntity(UserDto dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .encodedPassword(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    @Override
    @Deprecated
    public PaginatedResponseDto<UserDto> mapToPaginatedDto(Page<UserEntity> entityPage) {
        return null;
    }

}
