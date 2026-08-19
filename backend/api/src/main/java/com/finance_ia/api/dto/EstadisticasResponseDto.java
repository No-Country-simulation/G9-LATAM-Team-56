package com.finance_ia.api.dto;

import java.util.List;

public record EstadisticasResponseDto(
        List<TransaccionDetalleDto> transaccionesDetalladas,
        List<CategoryCardDto> top3
) {
    public record TransaccionDetalleDto(
            String fecha,
            Double valor,
            String categoria
    ) {}
}
