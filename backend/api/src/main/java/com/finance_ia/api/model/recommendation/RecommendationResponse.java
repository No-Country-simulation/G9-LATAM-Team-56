package com.finance_ia.api.model.recommendation;

import java.util.List;

public record RecommendationResponse(
        List<String> recommendations
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
