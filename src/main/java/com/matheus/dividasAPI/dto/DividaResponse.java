package com.matheus.dividasAPI.dto;

import com.matheus.dividasAPI.enums.DividaStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DividaResponse(
        Long id,
        String cpfDevedor,
        BigDecimal valorPego,
        BigDecimal valorComJuros,
        BigDecimal valorComDesconto,
        DividaStatus status,
        LocalDateTime dataCriacao,
        LocalDate dataVencimento
) {
}
