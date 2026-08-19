package com.finance_ia.api.service.recommendation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.finance_ia.api.config.RecommendationProperties;
import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.model.recommendation.TopCategory;

class RecommendationServiceTest {

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        RecommendationRuleCatalog catalog =
                new RecommendationRuleCatalog();

        RecommendationProperties properties =
                new RecommendationProperties();

        properties.setMaxRecommendations(3);

        service = new RecommendationService(
                catalog,
                properties
        );
    }

    @Test
    @DisplayName("Debe generar la cantidad esperada de recomendaciones a partir de las principales categorías")
    void shouldGenerateRecommendationsFromTopCategories() {

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.RESTAURANT,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.TRANSPORTE,
                                        2
                                ),
                                new TopCategory(
                                        ExpenseCategory.SALUD,
                                        3
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertEquals(
                3,
                response.recommendations().size()
        );
    }

    @Test
    @DisplayName("Debe anteponer las recomendaciones de prioridad ALTA sobre las de menor prioridad")
    void shouldPrioritizeHighPriorityRecommendations() {

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.SALUD,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.RESTAURANT,
                                        2
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertEquals(
                "Priorizar la reducción de gastos en restaurantes y limitar el consumo fuera del hogar mientras se estabiliza la situación financiera.",
                response.recommendations().get(0).recommendation()
        );
    }

    @Test
    @DisplayName("Debe utilizar la posición del ranking como criterio de desempate cuando tienen la misma prioridad")
    void shouldUseTopPositionAsTieBreaker() {

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.VIVIENDA,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.RESTAURANT,
                                        2
                                ),
                                new TopCategory(
                                        ExpenseCategory.TRANSPORTE,
                                        3
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertTrue(
                response.recommendations().get(0).recommendation().contains(
                        "gasto de vivienda"
                )
        );

        assertTrue(
                response.recommendations().get(1).recommendation().contains(
                        "gastos en restaurantes"
                )
        );

        assertTrue(
                response.recommendations().get(2).recommendation().contains(
                        "transporte"
                )
        );
    }

    @Test
    @DisplayName("Debe limitar la cantidad de resultados devueltos al valor máximo configurado")
    void shouldRespectMaxRecommendations() {

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.RESTAURANT,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.TRANSPORTE,
                                        2
                                ),
                                new TopCategory(
                                        ExpenseCategory.VIVIENDA,
                                        3
                                ),
                                new TopCategory(
                                        ExpenseCategory.ENTRETENIMIENTO,
                                        4
                                ),
                                new TopCategory(
                                        ExpenseCategory.ELECTRONICOS,
                                        5
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertEquals(
                3,
                response.recommendations().size()
        );
    }

    @Test
    @DisplayName("Debe retornar todas las recomendaciones candidatas si el máximo configurado es superior a las disponibles")
    void shouldReturnAllCandidatesWhenMaxIsGreaterThanAvailable() {

        RecommendationRuleCatalog catalog =
                new RecommendationRuleCatalog();

        RecommendationProperties properties =
                new RecommendationProperties();

        properties.setMaxRecommendations(10);

        RecommendationService service =
                new RecommendationService(
                        catalog,
                        properties
                );
        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.SALUDABLE,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.VIVIENDA,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.SALUD,
                                        2
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertEquals(
                2,
                response.recommendations().size()
        );
    }

    @Test
    @DisplayName("Debe asignar prioridad MEDIA a Salud y ordenar correctamente ante Restaurante (prioridad ALTA)")
    void shouldApplyMediumPriorityToHealthInRiskProfile() {

        RecommendationRequest request =
                new RecommendationRequest(
                        FinancialProfile.RIESGO,
                        List.of(
                                new TopCategory(
                                        ExpenseCategory.SALUD,
                                        1
                                ),
                                new TopCategory(
                                        ExpenseCategory.RESTAURANT,
                                        2
                                )
                        )
                );

        RecommendationResponse response =
                service.generateRecommendations(request);

        assertTrue(
                response.recommendations().get(0).recommendation().contains(
                        "restaurantes"
                )
        );

        assertTrue(
                response.recommendations().get(1).recommendation().contains(
                        "gastos de salud"
                )
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la solicitud de recomendación es nula")
    void shouldRejectNullRequest() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.generateRecommendations(null)
        );
    }
}