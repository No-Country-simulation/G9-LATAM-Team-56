package com.finance_ia.api.dto.recommendation;

import com.finance_ia.api.model.recommendation.FinancialProfile;

import java.util.List;

public record RecommendationRequestDto(
        FinancialProfile profile,
        List<TopCategoryDto> topCategories
) {

    public RecommendationRequestDto {
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