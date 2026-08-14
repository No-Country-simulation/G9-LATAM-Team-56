package com.finance_ia.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties(RecommendationProperties.class)
class RecommendationPropertiesSpringTest {

    @Autowired
    private RecommendationProperties properties;

    @Test
    void shouldLoadMaxRecommendationsFromApplicationProperties() {

        assertEquals(
                3,
                properties.getMaxRecommendations()
        );
    }
}