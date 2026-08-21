package com.finance_ia.api.model.recommendation;

import com.finance_ia.api.dto.recommendation.RecommendationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationResponseTest {

    @Test
    @DisplayName("Debe crear una respuesta válida con una lista de recomendaciones")
    void shouldCreateResponseWithRecommendations() {

        List<RecommendationResult> recommendations = List.of(
                new RecommendationResult(
                        ExpenseCategory.RESTAURANT,
                        "Revisar gastos de restaurante."
                ),
                new RecommendationResult(
                        ExpenseCategory.TRANSPORTE,
                        "Optimizar gastos de transporte."
                )
        );

        RecommendationResponse response =
                new RecommendationResponse(recommendations);

        assertEquals(
                recommendations,
                response.recommendations()
        );
    }

    @Test
    @DisplayName("Debe permitir crear una respuesta con una lista de recomendaciones vacía")
    void shouldAllowEmptyRecommendations() {

        RecommendationResponse response =
                new RecommendationResponse(List.of());

        assertTrue(
                response.recommendations().isEmpty()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la lista de recomendaciones es nula")
    void shouldRejectNullRecommendations() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationResponse(null)
        );
    }

    @Test
    @DisplayName("Debe proteger la lista de recomendaciones contra modificaciones externas (Inmutabilidad)")
    void shouldProtectRecommendationsFromExternalModification() {

        List<RecommendationResult> recommendations = new ArrayList<>();

        recommendations.add(
                new RecommendationResult(
                        ExpenseCategory.RESTAURANT,
                        "Revisar gastos de restaurante."
                )
        );

        RecommendationResponse response =
                new RecommendationResponse(recommendations);

        recommendations.add(
                new RecommendationResult(
                        ExpenseCategory.TRANSPORTE,
                        "Optimizar gastos de transporte."
                )
        );

        assertEquals(
                1,
                response.recommendations().size()
        );
    }
}