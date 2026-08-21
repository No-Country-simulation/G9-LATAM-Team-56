package com.finance_ia.api.dto.recommendation;

import com.finance_ia.api.model.recommendation.ExpenseCategory;

public record RecommendationResult(
    ExpenseCategory category,
    String recommendation
) {

  public RecommendationResult {
    if (category == null) {
      throw new IllegalArgumentException(
          "La categoría es obligatoria"
      );
    }

    if (recommendation == null || recommendation.isBlank()) {
      throw new IllegalArgumentException(
          "La recomendación es obligatoria"
      );
    }
  }
}
