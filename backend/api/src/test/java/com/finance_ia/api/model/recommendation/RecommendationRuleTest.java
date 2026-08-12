package com.finance_ia.api.model.recommendation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationRuleTest {

    @Test
    @DisplayName("Debe crear una regla de recomendación válida con sus atributos correctamente asignados")
    void shouldCreateValidRecommendationRule() {

        RecommendationRule rule = new RecommendationRule(
                "Mantener un presupuesto definido para entretenimiento.",
                RecommendationPriority.BAJA
        );

        assertEquals(
                "Mantener un presupuesto definido para entretenimiento.",
                rule.recommendation()
        );

        assertEquals(
                RecommendationPriority.BAJA,
                rule.priority()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la recomendación es nula")
    void shouldRejectNullRecommendation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRule(
                        null,
                        RecommendationPriority.BAJA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la recomendación es una cadena vacía")
    void shouldRejectEmptyRecommendation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRule(
                        "",
                        RecommendationPriority.BAJA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la recomendación solo contiene espacios en blanco")
    void shouldRejectBlankRecommendation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRule(
                        "    ",
                        RecommendationPriority.BAJA
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la prioridad es nula")
    void shouldRejectNullPriority() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRule(
                        "Mantener un presupuesto definido.",
                        null
                )
        );
    }
}