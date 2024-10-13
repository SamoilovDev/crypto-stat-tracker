package com.samoilov.dev.cryptostattracker.controller.api;

import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyDto;
import com.samoilov.dev.cryptostattracker.dto.CryptoCurrencyStatisticsDto;
import com.samoilov.dev.cryptostattracker.dto.PaginatedResponseDto;
import com.samoilov.dev.cryptostattracker.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequestMapping("/crypto-tracker-api/v1/currencies")
public interface CryptoCurrencyApi {

    @GetMapping("/followed")
    ResponseEntity<PaginatedResponseDto<CryptoCurrencyDto>> retrieveAllFollowedCryptoCurrencies(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @AuthenticationPrincipal UserEntity authorizedUser);

    @PutMapping("/{currencyCode}/follow")
    ResponseEntity<Void> followCryptoCurrency(
            @PathVariable("currencyCode") String currencyCode,
            @RequestParam("checkingPeriod") Integer checkingPeriod, // in minutes
            @AuthenticationPrincipal UserEntity authorizedUser);

    @GetMapping("/{currencyCode}/statistics")
    ResponseEntity<CryptoCurrencyStatisticsDto> retrieveCryptoCurrencyStatisticsForPeriod(
            @PathVariable("currencyCode") String currencyCode,
            @RequestParam(value = "startOfPeriod", required = false) LocalDateTime startOfPeriod,
            @RequestParam(value = "endOfPeriod", required = false) LocalDateTime endOfPeriod,
            @AuthenticationPrincipal UserEntity authorizedUser);

}
