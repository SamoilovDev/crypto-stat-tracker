package com.samoilov.dev.cryptostattracker.controller.api;

import com.samoilov.dev.cryptostattracker.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crypto-tracker-api/v1/users")
@Tag(name = "User Management API", description = "API for user registration and management.")
public interface UserManagementApi {

    @Operation(
            summary = "Register a new user",
            description = "Allows a new user to register. This endpoint is accessible to everyone.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @PostMapping("/register")
    ResponseEntity<UserDto> registerNewUser(@Validated @RequestBody UserDto userDto);

}
