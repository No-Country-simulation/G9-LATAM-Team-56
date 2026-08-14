package com.finance_ia.api.model.recommendation;

public record RecommendationRule(
        String recommendation,
        RecommendationPriority priority
) {

    public RecommendationRule {
        if (recommendation == null || recommendation.isBlank()) {
            throw new IllegalArgumentException(
                    "La recomendación es obligatoria"
            );
        }

        if (priority == null) {
            throw new IllegalArgumentException(
                    "La prioridad es obligatoria"
            );
        }
    }
}