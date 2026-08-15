package com.finance_ia.api.service.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import com.finance_ia.api.model.recommendation.RecommendationKey;
import com.finance_ia.api.model.recommendation.RecommendationPriority;
import com.finance_ia.api.model.recommendation.RecommendationRule;

class RecommendationRuleCatalogTest {

    private RecommendationRuleCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new RecommendationRuleCatalog();
    }

    @Test
    void shouldContainExactly33Rules() {
        assertEquals(33, catalog.size());
    }

    @Test
    @DisplayName("Debe existir una regla para cada combinación posible de categoría de gasto y perfil financiero")
    void shouldContainRuleForEveryCategoryAndProfileCombination() {

        for (ExpenseCategory category : ExpenseCategory.values()) {
            for (FinancialProfile profile : FinancialProfile.values()) {

                RecommendationKey key = new RecommendationKey(
                        category,
                        profile
                );

                assertTrue(
                        catalog.find(key).isPresent(),
                        "Falta la regla para: "
                                + category + " + " + profile
                );
            }
        }
    }

    @Test
    @DisplayName("Debe asignar prioridad ALTA a la categoría Restaurante con perfil en Riesgo")
    void shouldHaveHighPriorityForRestaurantRisk() {

        RecommendationKey key = new RecommendationKey(
                ExpenseCategory.RESTAURANT,
                FinancialProfile.RIESGO
        );

        RecommendationRule rule = catalog.find(key).orElseThrow();

        assertEquals(
                RecommendationPriority.ALTA,
                rule.priority()
        );
    }

    @Test
    @DisplayName("Debe asignar prioridad MEDIA a la categoría Salud con perfil en Riesgo")
    void shouldHaveMediumPriorityForHealthRisk() {

        RecommendationKey key = new RecommendationKey(
                ExpenseCategory.SALUD,
                FinancialProfile.RIESGO
        );

        RecommendationRule rule = catalog.find(key).orElseThrow();

        assertEquals(
                RecommendationPriority.MEDIA,
                rule.priority()
        );
    }

    @Test
    @DisplayName("Debe asignar prioridad MEDIA a la categoría Educación con perfil en Riesgo")
    void shouldHaveMediumPriorityForEducationRisk() {

        RecommendationKey key = new RecommendationKey(
                ExpenseCategory.EDUCACION,
                FinancialProfile.RIESGO
        );

        RecommendationRule rule = catalog.find(key).orElseThrow();

        assertEquals(
                RecommendationPriority.MEDIA,
                rule.priority()
        );
    }

    @Test
    @DisplayName("Debe asignar prioridad BAJA a todas las categorías cuando el perfil es Saludable")
    void shouldHaveLowPriorityForHealthyProfile() {

        for (ExpenseCategory category : ExpenseCategory.values()) {

            RecommendationRule rule = catalog.find(
                    new RecommendationKey(
                            category,
                            FinancialProfile.SALUDABLE
                    )
            ).orElseThrow();

            assertEquals(
                    RecommendationPriority.BAJA,
                    rule.priority(),
                    "Prioridad incorrecta para: " + category
            );
        }
    }

    @Test
    @DisplayName("Debe asignar prioridad MEDIA a todas las categorías cuando el perfil está En Observación")
    void shouldHaveMediumPriorityForObservationProfile() {

        for (ExpenseCategory category : ExpenseCategory.values()) {

            RecommendationRule rule = catalog.find(
                    new RecommendationKey(
                            category,
                            FinancialProfile.EN_OBSERVACION
                    )
            ).orElseThrow();

            assertEquals(
                    RecommendationPriority.MEDIA,
                    rule.priority(),
                    "Prioridad incorrecta para: " + category
            );
        }
    }
    @Test
    void shouldContainAll33Rules() {

        RecommendationRuleCatalog catalog =
                new RecommendationRuleCatalog();

        assertEquals(
                33,
                catalog.size()
        );
    }
}