package com.samoilov.dev.cryptostattracker.controller.api;


import com.samoilov.dev.cryptostattracker.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crypto-tracker-api/v1/users")
public interface UserManagementApi {

    @PostMapping("/register")
    ResponseEntity<UserDto> registerNewUser(@Validated @RequestBody UserDto userDto);

}
