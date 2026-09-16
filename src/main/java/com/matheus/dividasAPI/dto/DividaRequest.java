package com.matheus.dividasAPI.dto;

import com.matheus.dividasAPI.validation.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DividaRequest(

        @Schema(
                description = "CPF do devedor com 11 dígitos",
                example = "12345678901"
        )
        @NotBlank
        @ValidCpf
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
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
