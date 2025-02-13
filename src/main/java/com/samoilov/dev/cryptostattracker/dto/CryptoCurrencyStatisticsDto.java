package com.samoilov.dev.cryptostattracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samoilov.dev.cryptostattracker.dto.base.BaseCryptoCurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoCurrencyStatisticsDto extends BaseCryptoCurrencyDto {

    @JsonProperty("max_price")
    private CurrencyCheckResultDto maxPriceCheck;

    @JsonProperty("min_price")
    private CurrencyCheckResultDto minPriceCheck;

    @JsonProperty("avg_price")
    private Double avgPrice;

}
