package com.finance_ia.api.service.recommendation;

import com.finance_ia.api.config.RecommendationProperties;
import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.model.recommendation.TopCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceAllRulesTest {

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        RecommendationRuleCatalog catalog =
                new RecommendationRuleCatalog();

        RecommendationProperties properties =
                new RecommendationProperties();

        properties.setMaxRecommendations(5);

        service = new RecommendationService(
                catalog,
                properties
        );
    }

    @Test
    @DisplayName("Debe generar una recomendación válida para todas las combinaciones de categorías de gasto y perfiles financieros")
    void shouldGenerateRecommendationForAllCategoryProfileCombinations() {

        for (FinancialProfile profile : FinancialProfile.values()) {

            for (ExpenseCategory category : ExpenseCategory.values()) {

                RecommendationRequest request =
                        new RecommendationRequest(
                                profile,
                                List.of(
                                        new TopCategory(
                                                category,
                                                1
                                        )
                                )
                        );

                RecommendationResponse response =
                        service.generateRecommendations(request);

                assertNotNull(
                        response,
                        "El response no debería ser null para "
                                + category + " + " + profile
                );

                assertEquals(
                        1,
                        response.recommendations().size(),
                        "Debe existir exactamente una recomendación para "
                                + category + " + " + profile
                );

                assertNotNull(
                        response.recommendations()
                                .get(0)
                                .recommendation(),
                        "La recomendación no debería ser null para "
                                + category + " + " + profile
                );

                assertFalse(
                        response.recommendations()
                                .get(0)
                                .recommendation().isBlank(),
                        "La recomendación no debería estar vacía para "
                                + category + " + " + profile
                );
            }
        }
    }
}