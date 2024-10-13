package com.samoilov.dev.cryptostattracker.controller;

import com.samoilov.dev.cryptostattracker.controller.api.UserManagementApi;
import com.samoilov.dev.cryptostattracker.dto.UserDto;
import com.samoilov.dev.cryptostattracker.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class UserManagementController implements UserManagementApi {

    private final UserManagementService userManagementService;

    @Override
    public ResponseEntity<UserDto> registerNewUser(UserDto userDto) {
        return ResponseEntity
                .status(CREATED)
                .body(userManagementService.registerNewUser(userDto));
    }

}
