package com.finance_ia.api.model.recommendation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationRequestTest {

    @Test
    @DisplayName("Debe crear una solicitud válida con perfil financiero y lista de categorías principales")
    void shouldCreateValidRequest() {

        List<TopCategory> categories = List.of(
                new TopCategory(
                        ExpenseCategory.RESTAURANTE,
                        1
                ),
                new TopCategory(
                        ExpenseCategory.TRANSPORTE,
                        2
                ),
                new TopCategory(
                        ExpenseCategory.VIVIENDA,
                        3
                )
        );

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        categories
                );

        assertEquals(
                FinancialProfile.RIESGO,
                request.profile()
        );

        assertEquals(
                categories,
                request.topCategories()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el perfil financiero es nulo")
    void shouldRejectNullProfile() {

        List<TopCategory> categories = List.of(
                new TopCategory(
                        ExpenseCategory.RESTAURANTE,
                        1
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequest(
                        null,
                        categories
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la lista de categorías principales es nula")
    void shouldRejectNullTopCategories() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        null
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la lista de categorías principales está vacía")
    void shouldRejectEmptyTopCategories() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of()
                )
        );
    }

    @Test
    @DisplayName("Debe proteger la lista de categorías contra modificaciones externas posteriores (Inmutabilidad)")
    void shouldProtectTopCategoriesFromExternalModification() {

        List<TopCategory> categories = new ArrayList<>();

        categories.add(
                new TopCategory(
                        ExpenseCategory.RESTAURANTE,
                        1
                )
        );

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        categories
                );

        // Modificamos la lista original después de crear el request.
        categories.add(
                new TopCategory(
                        ExpenseCategory.TRANSPORTE,
                        2
                )
        );

        // El request conserva solamente la información original.
        assertEquals(
                1,
                request.topCategories().size()
        );
    }
}