package com.finance_ia.api.infra.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato estándar de respuesta de error de la API.
 * El campo {@code errors} permite devolver múltiples errores
 * de validación en una misma respuesta.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<ErrorDetail> errors
) {

    public ErrorResponse {
        errors = errors == null
                ? List.of()
                : List.copyOf(errors);
    }
}
