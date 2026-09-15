package com.matheus.dividasAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DividaRequest(
        @Schema(
                description = "CPF do devedor com 11 dígitos",
                example = "12345678901"
        )
        @NotBlank
        @Size(min = 11, max = 11)
        String cpfDevedor,

        @Schema(
                description = "Valor original da dívida",
                example = "1000.00"
        )
        @NotNull
        @Positive
        BigDecimal valorPego,

        @Schema(
                description = "Valor da dívida com juros aplicados",
                example = "1200.00"
        )
        @NotNull
        @Positive
        BigDecimal valorComJuros,

        @Schema(
                description = "Valor da dívida com desconto aplicado",
                example = "1100.00"
        )
        @NotNull
        @Positive
        BigDecimal valorComDesconto
) {
}
