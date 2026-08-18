package com.finance_ia.api.controller;

import com.finance_ia.api.dto.recommendation.RecommendationRequestDto;
import com.finance_ia.api.dto.recommendation.RecommendationResponseDto;
import com.finance_ia.api.dto.recommendation.RecommendationResult;
import com.finance_ia.api.dto.recommendation.TopCategoryDto;
import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.model.recommendation.TopCategory;
import com.finance_ia.api.service.recommendation.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService
    ) {
        if (recommendationService == null) {
            throw new IllegalArgumentException(
                    "El servicio de recomendaciones es obligatorio"
            );
        }

        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<RecommendationResponseDto> generateRecommendations(
            @RequestBody RecommendationRequestDto requestDto
    ) {

        RecommendationRequest request = toDomain(requestDto);

        RecommendationResponse response =
                recommendationService.generateRecommendations(request);

        RecommendationResponseDto responseDto =
                toDto(response);

        return ResponseEntity.ok(responseDto);
    }

    private RecommendationRequest toDomain(RecommendationRequestDto dto) {

        List<TopCategory> topCategories =
                dto.topCategories()
                        .stream()
                        .map(this::toDomain)
                        .toList();

        return new RecommendationRequest(
                dto.profile(),
                topCategories
        );
    }

    private TopCategory toDomain(TopCategoryDto dto) {
        return new TopCategory(
                dto.category(),
                dto.position()
        );
    }

    private RecommendationResponseDto toDto(
            RecommendationResponse response
    ) {

        List<String> recomendaciones =
                response.recommendations()
                        .stream()
                        .map(RecommendationResult::recommendation)
                        .toList();

        return new RecommendationResponseDto(
                recomendaciones
        );
    }
}