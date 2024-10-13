package com.samoilov.dev.cryptostattracker.service.impl;

import com.samoilov.dev.cryptostattracker.dto.UserDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import com.samoilov.dev.cryptostattracker.mapper.Mapper;
import com.samoilov.dev.cryptostattracker.repository.UserRepository;
import com.samoilov.dev.cryptostattracker.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists";

    private final Mapper<UserEntity, UserDto> userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ResponseStatusException(CONFLICT, USERNAME_ALREADY_EXISTS_MESSAGE);
        }

        UserEntity savedUser = userRepository.save(userMapper.mapToEntity(userDto));

        return userMapper.mapToDto(savedUser);
    }

}
