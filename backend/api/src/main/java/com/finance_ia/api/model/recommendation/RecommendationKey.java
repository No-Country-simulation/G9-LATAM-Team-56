package com.finance_ia.api.model.recommendation;

public record RecommendationKey(
        ExpenseCategory category,
        FinancialProfile profile
) {

    public RecommendationKey {
        if (category == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria"
            );
        }

        if (profile == null) {
            throw new IllegalArgumentException(
                    "El perfil financiero es obligatorio"
            );
        }
    }
}