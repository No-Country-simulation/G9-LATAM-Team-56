package com.finance_ia.api.model.recommendation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationCandidateTest {

    @Test
    @DisplayName("Debe crear un candidato de recomendación válido con todos sus atributos")
    void shouldCreateValidCandidate() {

        RecommendationCandidate candidate =
                new RecommendationCandidate(
                        ExpenseCategory.RESTAURANTE,
                        1,
                        "Reducir la frecuencia de comidas fuera del hogar.",
                        RecommendationPriority.MEDIA
                );

        assertEquals(
                ExpenseCategory.RESTAURANTE,
                candidate.category()
        );

        assertEquals(
                1,
                candidate.position()
        );

        assertEquals(
                "Reducir la frecuencia de comidas fuera del hogar.",
                candidate.recommendation()
        );

        assertEquals(
                RecommendationPriority.MEDIA,
                candidate.priority()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la categoría de gasto es nula")
    void shouldRejectNullCategory() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationCandidate(
                        null,
                        1,
                        "Recomendación de prueba",
                        RecommendationPriority.MEDIA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la posición es inválida (menor o igual a cero)")
    void shouldRejectInvalidPosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationCandidate(
                        ExpenseCategory.RESTAURANTE,
                        0,
                        "Recomendación de prueba",
                        RecommendationPriority.MEDIA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el texto de la recomendación está en blanco")
    void shouldRejectBlankRecommendation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationCandidate(
                        ExpenseCategory.RESTAURANTE,
                        1,
                        "   ",
                        RecommendationPriority.MEDIA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la prioridad es nula")
    void shouldRejectNullPriority() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationCandidate(
                        ExpenseCategory.RESTAURANTE,
                        1,
                        "Recomendación de prueba",
                        null
                )
        );
    }
}