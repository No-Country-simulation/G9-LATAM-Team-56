package com.finance_ia.api.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.service.recommendation.RecommendationService;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @Test
    @DisplayName("Debe generar recomendaciones exitosamente cuando la solicitud es válida")
    void shouldGenerateRecommendations() throws Exception {

        RecommendationResponse serviceResponse =
                new RecommendationResponse(
                        List.of(
                                "Revisar gastos de restaurante.",
                                "Optimizar gastos de transporte."
                        )
                );

        when(
                recommendationService.generateRecommendations(any(
                        RecommendationRequest.class
                ))
        ).thenReturn(serviceResponse);

        String requestJson = """
                {
                    "profile": "RIESGO",
                    "topCategories": [
                        {
                            "category": "RESTAURANT",
                            "position": 1
                        },
                        {
                            "category": "TRANSPORTE",
                            "position": 2
                        }
                    ]
                }
                """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.recomendaciones").isArray())
                .andExpect(jsonPath("$.recomendaciones.length()").value(2))
                .andExpect(jsonPath("$.recomendaciones[0]")
                        .value("Revisar gastos de restaurante."))
                .andExpect(jsonPath("$.recomendaciones[1]")
                        .value("Optimizar gastos de transporte."));

        verify(
                recommendationService
        ).generateRecommendations(any(RecommendationRequest.class));
    }

    @Test
    @DisplayName("Debe retornar Bad Request (400) cuando falta el perfil financiero")
    void shouldReturnBadRequestWhenProfileIsMissing() throws Exception {

        String requestJson = """
            {
                "topCategories": [
                    {
                        "category": "RESTAURANT",
                        "position": 1
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar Bad Request (400) cuando falta la lista de categorías principales")
    void shouldReturnBadRequestWhenTopCategoriesAreMissing() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO"
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar Bad Request (400) cuando la lista de categorías principales está vacía")
    void shouldReturnBadRequestWhenTopCategoriesAreEmpty() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": []
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar Bad Request (400) cuando el valor de la categoría no es válido")
    void shouldReturnBadRequestWhenCategoryIsInvalid() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "CATEGORIA_INEXISTENTE",
                        "position": 1
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar Bad Request (400) cuando la posición de la categoría no es válida")
    void shouldReturnBadRequestWhenPositionIsInvalid() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "RESTAURANT",
                        "position": 0
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe delegar la llamada al servicio de recomendaciones")
    void shouldDelegateRequestToRecommendationService() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "RESTAURANT",
                        "position": 1
                    },
                    {
                        "category": "TRANSPORTE",
                        "position": 2
                    }
                ]
            }
            """;

        when(recommendationService.generateRecommendations(any()))
                .thenReturn(
                        new RecommendationResponse(
                                List.of(
                                        "Priorizar la reducción de gastos en restaurantes.",
                                        "Priorizar alternativas de menor costo en transporte."
                                )
                        )
                );

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(recommendationService)
                .generateRecommendations(any());
    }

    @Test
    @DisplayName("Debe mapear e incluir el listado de recomendaciones en la respuesta HTTP")
    void shouldReturnRecommendationsInHttpResponse() throws Exception {

        when(recommendationService.generateRecommendations(any()))
                .thenReturn(
                        new RecommendationResponse(
                                List.of(
                                        "Priorizar la reducción de gastos en restaurantes.",
                                        "Priorizar alternativas de menor costo en transporte."
                                )
                        )
                );

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "RESTAURANT",
                        "position": 1
                    },
                    {
                        "category": "TRANSPORTE",
                        "position": 2
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/recomendaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recomendaciones").isArray())
                .andExpect(jsonPath("$.recomendaciones.length()").value(2))
                .andExpect(jsonPath("$.recomendaciones[0]")
                        .value("Priorizar la reducción de gastos en restaurantes."))
                .andExpect(jsonPath("$.recomendaciones[1]")
                        .value("Priorizar alternativas de menor costo en transporte."));
    }
}