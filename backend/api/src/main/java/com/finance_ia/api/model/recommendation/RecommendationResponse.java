package com.finance_ia.api.model.recommendation;

import com.finance_ia.api.dto.recommendation.RecommendationResult;

import java.util.List;

public record RecommendationResponse(
    List<RecommendationResult> recommendations
) {

    public RecommendationResponse {
        if (recommendations == null) {
            throw new IllegalArgumentException(
                "La lista de recomendaciones es obligatoria"
            );
        }

        recommendations = List.copyOf(recommendations);
    }
}
