package com.finance_ia.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "recommendation")
public class RecommendationProperties {

    private int maxRecommendations = 3;
}
