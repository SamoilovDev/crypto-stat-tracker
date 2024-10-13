package com.samoilov.dev.cryptostattracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaginatedResponseDto<T extends Serializable> implements Serializable {

    private int page;

    private int size;

    private long total;

    private List<T> data;

}
