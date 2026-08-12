package com.finance_ia.api.dto.recommendation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationResponseDtoTest {

    @Test
    void shouldCreateResponseWithRecommendations() {

        List<String> recommendations = List.of(
                "Revisar gastos de restaurante.",
                "Optimizar gastos de transporte."
        );

        RecommendationResponseDto response =
                new RecommendationResponseDto(
                        recommendations
                );

        assertEquals(
                recommendations,
                response.recomendaciones()
        );
    }

    @Test
    void shouldAllowEmptyRecommendations() {

        RecommendationResponseDto response =
                new RecommendationResponseDto(
                        List.of()
                );

        assertTrue(
                response.recomendaciones().isEmpty()
        );
    }

    @Test
    void shouldRejectNullRecommendations() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationResponseDto(null)
        );
    }

    @Test
    void shouldProtectRecommendationsFromExternalModification() {

        List<String> recommendations = new ArrayList<>();

        recommendations.add(
                "Revisar gastos de restaurante."
        );

        RecommendationResponseDto response =
                new RecommendationResponseDto(
                        recommendations
                );

        recommendations.add(
                "Optimizar gastos de transporte."
        );

        assertEquals(
                1,
                response.recomendaciones().size()
        );
    }
}