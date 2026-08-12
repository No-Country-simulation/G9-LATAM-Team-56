package com.finance_ia.api.model.recommendation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationKeyTest {

    @Test
    @DisplayName("Debe crear una clave de recomendación válida con atributos correctos")
    void shouldCreateValidRecommendationKey() {

        RecommendationKey key = new RecommendationKey(
                ExpenseCategory.RESTAURANTE,
                FinancialProfile.RIESGO
        );

        assertEquals(
                ExpenseCategory.RESTAURANTE,
                key.category()
        );

        assertEquals(
                FinancialProfile.RIESGO,
                key.profile()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la categoría de gasto es nula")
    void shouldRejectNullCategory() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationKey(
                        null,
                        FinancialProfile.RIESGO
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el perfil financiero es nulo")
    void shouldRejectNullProfile() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationKey(
                        ExpenseCategory.RESTAURANTE,
                        null
                )
        );
    }

    @Test
    @DisplayName("Debe evaluar como iguales dos instancias con la misma categoría y perfil")
    void shouldConsiderEqualKeysAsEqual() {

        RecommendationKey key1 = new RecommendationKey(
                ExpenseCategory.RESTAURANTE,
                FinancialProfile.RIESGO
        );

        RecommendationKey key2 = new RecommendationKey(
                ExpenseCategory.RESTAURANTE,
                FinancialProfile.RIESGO
        );

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }
}