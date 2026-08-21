package com.finance_ia.api.model.recommendation;

public record RecommendationCandidate(
        ExpenseCategory category,
        int position,
        String recommendation,
        RecommendationPriority priority
) {

    public RecommendationCandidate {
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