package com.samoilov.dev.cryptostattracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samoilov.dev.cryptostattracker.dto.base.BaseCryptoCurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoCurrencyDto extends BaseCryptoCurrencyDto {

    @JsonProperty("current_price")
    private Double currentPrice;

    @JsonProperty("last_update_time")
    private LocalDateTime lastUpdateTime;

    @JsonProperty("checking_period")
    private Integer checkingPeriod;

}
