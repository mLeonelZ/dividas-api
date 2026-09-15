package com.matheus.dividasAPI.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErrorResponse(
        @Schema(
                description = "Código HTTP referente ao erro",
                example = "404"
        )
        int status,

        @Schema(
                description = "Mensagem descritiva do erro",
                example = "Divida não encontrada: 999"
        )
        String message,

        @Schema(
                description = "Data e hora em que o erro ocorreu",
                example = "2026-09-15T19:30:00"
        )
        LocalDateTime timestamp
) {
}
