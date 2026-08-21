package com.finance_ia.api.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecommendationPropertiesTest {

    @Test
    void shouldHaveDefaultMaxRecommendations() {

        RecommendationProperties properties =
                new RecommendationProperties();

        assertEquals(
                3,
                properties.getMaxRecommendations()
        );
    }

    @Test
    void shouldAllowChangingMaxRecommendations() {

        RecommendationProperties properties =
                new RecommendationProperties();

        properties.setMaxRecommendations(5);

        assertEquals(
                5,
                properties.getMaxRecommendations()
        );
    }
}