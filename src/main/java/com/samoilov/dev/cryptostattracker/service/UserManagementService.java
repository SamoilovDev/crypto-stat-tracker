package com.samoilov.dev.cryptostattracker.service;

import com.samoilov.dev.cryptostattracker.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserManagementService extends UserDetailsService {

    @Transactional
    UserDto registerNewUser(UserDto userDto);

}
