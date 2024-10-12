package com.samoilov.dev.cryptostattracker.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseCryptoCurrencyDto implements Serializable {

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("currency_name")
    private String currencyName;

}
