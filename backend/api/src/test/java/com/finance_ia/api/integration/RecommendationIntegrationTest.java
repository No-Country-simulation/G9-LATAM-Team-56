package com.finance_ia.api.integration;

import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecommendationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGenerateRecommendationsThroughFullApplication() {

        String requestJson = """
                {
                    "profile": "RIESGO",
                    "topCategories": [
                        {
                            "category": "RESTAURANTE",
                            "position": 1
                        },
                        {
                            "category": "TRANSPORTE",
                            "position": 2
                        },
                        {
                            "category": "VIVIENDA",
                            "position": 3
                        }
                    ]
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/recomendaciones",
                        request,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertTrue(
                response.getBody().contains("restaurantes")
        );
    }


    @Test
    void shouldGenerateRecommendationForAll33CategoryProfileCombinations() {

        for (ExpenseCategory category : ExpenseCategory.values()) {

            for (FinancialProfile profile : FinancialProfile.values()) {

                String requestJson = """
                    {
                        "profile": "%s",
                        "topCategories": [
                            {
                                "category": "%s",
                                "position": 1
                            }
                        ]
                    }
                    """.formatted(
                        profile.name(),
                        category.name()
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request =
                        new HttpEntity<>(requestJson, headers);

                ResponseEntity<String> response =
                        restTemplate.postForEntity(
                                "http://localhost:" + port + "/api/recomendaciones",
                                request,
                                String.class
                        );

                assertEquals(
                        HttpStatus.OK,
                        response.getStatusCode(),
                        "Falló la combinación: "
                                + category + " + " + profile
                );

                assertNotNull(response.getBody());

                assertTrue(
                        response.getBody().contains("recomendaciones"),
                        "La respuesta no contiene recomendaciones para: "
                                + category + " + " + profile
                );
            }
        }
    }

    @Test
    void shouldPrioritizeRecommendationBeforeTopPosition() {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "SALUD",
                        "position": 1
                    },
                    {
                        "category": "RESTAURANTE",
                        "position": 2
                    }
                ]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/recomendaciones",
                        request,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        String body = response.getBody();

        int restaurantPosition =
                body.indexOf("restaurantes");

        int healthPosition =
                body.indexOf("gastos de salud");

        assertTrue(
                restaurantPosition >= 0,
                "La recomendación de restaurante no fue encontrada"
        );

        assertTrue(
                healthPosition >= 0,
                "La recomendación de salud no fue encontrada"
        );

        assertTrue(
                restaurantPosition < healthPosition,
                "La recomendación de mayor prioridad debe aparecer primero"
        );
    }
    @Test
    void shouldUseTopPositionWhenPrioritiesAreEqual() {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "RESTAURANTE",
                        "position": 2
                    },
                    {
                        "category": "TRANSPORTE",
                        "position": 3
                    },
                    {
                        "category": "VIVIENDA",
                        "position": 1
                    }
                ]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/recomendaciones",
                        request,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        String body = response.getBody();

        int housingPosition =
                body.indexOf("gasto de vivienda");

        int restaurantPosition =
                body.indexOf("gastos en restaurantes");

        int transportPosition =
                body.indexOf("gastos de transporte");

        assertTrue(housingPosition >= 0);
        assertTrue(restaurantPosition >= 0);
        assertTrue(transportPosition >= 0);

        assertTrue(
                housingPosition < restaurantPosition,
                "Vivienda debe aparecer antes que Restaurante"
        );

        assertTrue(
                restaurantPosition < transportPosition,
                "Restaurante debe aparecer antes que Transporte"
        );
    }

    @Test
    void shouldRespectMaxRecommendationsThroughHttp() throws Exception {

        String requestJson = """
            {
                "profile": "RIESGO",
                "topCategories": [
                    {
                        "category": "RESTAURANTE",
                        "position": 1
                    },
                    {
                        "category": "TRANSPORTE",
                        "position": 2
                    },
                    {
                        "category": "VIVIENDA",
                        "position": 3
                    },
                    {
                        "category": "ENTRETENIMIENTO",
                        "position": 4
                    },
                    {
                        "category": "ELECTRONICOS",
                        "position": 5
                    }
                ]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/recomendaciones",
                        request,
                        String.class
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        JsonNode json =
                objectMapper.readTree(response.getBody());

        JsonNode recommendations =
                json.get("recomendaciones");

        assertNotNull(recommendations);
        assertTrue(recommendations.isArray());

        assertEquals(
                3,
                recommendations.size()
        );
    }
}