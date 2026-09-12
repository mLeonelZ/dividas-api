package com.matheus.dividasAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DividaRequest(
        @NotBlank
        @Size(min = 11, max = 11)
        String cpfDevedor,

        @NotNull
        @Positive
        BigDecimal valorPego
) {
}
