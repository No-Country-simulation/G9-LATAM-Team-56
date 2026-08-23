package com.finance_ia.api.infra.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "ErrorResponse",
        description = "Estructura estándar utilizada para informar errores de la API."
)
public record ErrorResponse(

        @Schema(
                description = "Fecha y hora en la que ocurrió el error.",
                example = "2026-08-23T10:30:00"
        )
        LocalDateTime timestamp,

        @Schema(
                description = "Código HTTP correspondiente al error.",
                example = "400"
        )
        int status,

        @Schema(
                description = "Código identificador del tipo de error.",
                example = "VALIDATION_ERROR"
        )
        String error,

        @Schema(
                description = "Descripción general del error.",
                example = "Los datos enviados no son válidos."
        )
        String message,

        @Schema(
                description = "Lista de errores específicos encontrados durante la validación."
        )
        List<ErrorDetail> errors
) {

    public ErrorResponse {
        errors = errors == null
                ? List.of()
                : List.copyOf(errors);
    }
}
