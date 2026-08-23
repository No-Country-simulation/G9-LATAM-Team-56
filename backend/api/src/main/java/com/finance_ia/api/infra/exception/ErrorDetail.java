package com.finance_ia.api.infra.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorDetail",
        description = "Detalle específico de un error de validación."
)
public record ErrorDetail(

        @Schema(
                description = "Campo que produjo el error.",
                example = "ingreso_mensual"
        )
        String field,

        @Schema(
                description = "Descripción del problema detectado.",
                example = "El ingreso mensual debe ser mayor a 0."
        )
        String message,

        @Schema(
                description = "Número de fila del archivo CSV donde se produjo el error. Puede ser null cuando el error no proviene de un archivo.",
                example = "5",
                nullable = true
        )
        Integer row
) {
}
