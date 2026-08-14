package com.finance_ia.api.dto.recommendation;

import java.util.List;

public record RecommendationResponseDto(
        List<String> recomendaciones
) {

    public RecommendationResponseDto {
        if (recomendaciones == null) {
            throw new IllegalArgumentException(
                    "La lista de recomendaciones es obligatoria"
            );
        }

        recomendaciones = List.copyOf(recomendaciones);
    }
}