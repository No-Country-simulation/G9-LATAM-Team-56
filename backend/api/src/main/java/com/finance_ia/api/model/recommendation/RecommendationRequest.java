package com.finance_ia.api.model.recommendation;

import java.util.List;

public record RecommendationRequest(
        FinancialProfile profile,
        List<TopCategory> topCategories
) {

    public RecommendationRequest {
        if (profile == null) {
            throw new IllegalArgumentException(
                    "El perfil financiero es obligatorio"
            );
        }

        if (topCategories == null || topCategories.isEmpty()) {
            throw new IllegalArgumentException(
                    "Las categorías TOP son obligatorias"
            );
        }

        topCategories = List.copyOf(topCategories);
    }
}
