package com.finance_ia.api.dto.recommendation;

import com.finance_ia.api.model.recommendation.ExpenseCategory;

public record TopCategoryDto(
        ExpenseCategory category,
        int position
) {

    public TopCategoryDto {
        if (category == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria"
            );
        }

        if (position < 1) {
            throw new IllegalArgumentException(
                    "La posición debe ser mayor o igual a 1"
            );
        }
    }
}