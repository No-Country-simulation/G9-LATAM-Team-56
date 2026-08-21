package com.finance_ia.api.model.recommendation;

public record TopCategory(
        ExpenseCategory category,
        int position
) {
    public TopCategory {
        if (category == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        if (position < 1) {
            throw new IllegalArgumentException(
                    "La posición debe ser mayor o igual a 1"
            );
        }
    }
}