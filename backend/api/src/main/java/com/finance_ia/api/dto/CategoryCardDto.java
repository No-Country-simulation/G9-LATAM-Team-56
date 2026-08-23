package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "CategoryCard",
        description = "Información resumida de una categoría de gasto."
)
public record CategoryCardDto(

        @Schema(
                description = "Nombre o etiqueta de la categoría.",
                example = "ALIMENTACION"
        )
        String label,

        @Schema(
                description = "Valor total asociado a la categoría.",
                example = "450.50"
        )
        String valor

) {
}
