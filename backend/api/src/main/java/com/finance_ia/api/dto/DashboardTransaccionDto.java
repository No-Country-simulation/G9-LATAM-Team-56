package com.finance_ia.api.dto;

import java.time.LocalDate;

public record DashboardTransaccionDto(
        String categoria,
        String descripcion,
        Double valor,
        LocalDate fecha
) {

    public DashboardTransaccionDto {

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria"
            );
        }

        if (descripcion == null) {
            throw new IllegalArgumentException(
                    "La descripción es obligatorio"
            );
        }

        if (valor == null) {
            throw new IllegalArgumentException(
                    "El valor es obligatorio"
            );
        }

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria"
            );
        }
    }
}