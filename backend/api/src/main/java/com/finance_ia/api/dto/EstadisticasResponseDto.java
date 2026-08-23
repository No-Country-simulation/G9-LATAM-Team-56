package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "EstadisticasResponse",
        description = "Estadísticas financieras de un usuario para un período determinado."
)
public record EstadisticasResponseDto(

        @Schema(
                description = "Lista de transacciones detalladas dentro del período consultado."
        )
        List<TransaccionDetalleDto> transaccionesDetalladas,

        @Schema(
                description = "Tres categorías con mayor gasto durante el período consultado."
        )
        List<CategoryCardDto> top3

) {

    @Schema(
            name = "TransaccionDetalle",
            description = "Información resumida de una transacción financiera."
    )
    public record TransaccionDetalleDto(

            @Schema(
                    description = "Fecha en la que se realizó la transacción.",
                    example = "2026-08-23"
            )
            String fecha,

            @Schema(
                    description = "Valor monetario de la transacción.",
                    example = "150.50"
            )
            Double valor,

            @Schema(
                    description = "Categoría financiera asignada a la transacción.",
                    example = "ALIMENTACION"
            )
            String categoria
    ) {}
}