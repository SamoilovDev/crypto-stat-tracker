package com.samoilov.dev.cryptostattracker.controller.api;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/crypto-tracker-api/v1/currencies")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@Tag(name = "CryptoCurrency API", description = "API for tracking and following cryptocurrency statistics.")
public interface CryptoCurrencyApi {

    @Operation(
            summary = "Retrieve all followed cryptocurrencies",
            description = "Returns a paginated list of followed cryptocurrencies for the authenticated user.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of followed cryptocurrencies",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping("/followed")
    ResponseEntity<PaginatedResponseDto<CryptoCurrencyDto>> retrieveAllFollowedCryptoCurrencies(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @AuthenticationPrincipal UserEntity authorizedUser);

    @Operation(
            summary = "Follow a cryptocurrency",
            description = "Start following a cryptocurrency with a specified checking period (in minutes).",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully followed the cryptocurrency"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @PutMapping("/{currencyCode}/follow")
    ResponseEntity<Void> followCryptoCurrency(
            @PathVariable("currencyCode") String currencyCode,
            @RequestParam("checkingPeriod") Integer checkingPeriod, // in minutes
            @AuthenticationPrincipal UserEntity authorizedUser);

    @Operation(
            summary = "Retrieve cryptocurrency statistics for a specific period",
            description = "Returns statistics for a cryptocurrency within a specified time range.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency statistics",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CryptoCurrencyStatisticsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            })
    @GetMapping("/{currencyCode}/statistics")
    ResponseEntity<CryptoCurrencyStatisticsDto> retrieveCryptoCurrencyStatisticsForPeriod(
            @PathVariable("currencyCode") String currencyCode,
            @RequestParam("startOfPeriod") LocalDateTime startOfPeriod,
            @RequestParam("endOfPeriod") LocalDateTime endOfPeriod,
            @AuthenticationPrincipal UserEntity authorizedUser);

}
