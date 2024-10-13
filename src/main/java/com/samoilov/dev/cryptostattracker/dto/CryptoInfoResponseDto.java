package com.samoilov.dev.cryptostattracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CryptoInfoResponseDto implements Serializable {

    private String id;

    private String symbol;

    private String name;

    @JsonProperty("current_price")
    private Double currentPrice;

}
