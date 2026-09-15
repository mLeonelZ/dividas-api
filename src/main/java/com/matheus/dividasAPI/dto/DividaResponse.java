package com.matheus.dividasAPI.dto;

import com.matheus.dividasAPI.enums.DividaStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DividaResponse(
        @Schema(
                description = "ID da dívida",
                example = "1"
        )
        Long id,
        @Schema(
                description = "CPF do devedor, deve conter 11 dígitos",
                example = "12345678901"
        )
        String cpfDevedor,

        @Schema(
                description = "Valor original da dívida",
                example = "1000.00"
        )
        BigDecimal valorPego,

        @Schema(
                description = "Valor da dívida com juros aplicados",
                example = "1200.00"
        )
        BigDecimal valorComJuros,

        @Schema(
                description = "Valor da dívida com desconto aplicado",
                example = "1100.00"
        )
        BigDecimal valorComDesconto,

        @Schema(
                description = "Situação atual da dívida",
                example = "PENDENTE"
        )
        DividaStatus status,

        @Schema(
                description = "Data e hora em que a dívida foi criada",
                example = "2026-09-15T10:30:00"
        )
        LocalDateTime dataCriacao,

        @Schema(
                description = "Data de vencimento da dívida",
                example = "2026-10-15"
        )
        LocalDate dataVencimento
) {
}
