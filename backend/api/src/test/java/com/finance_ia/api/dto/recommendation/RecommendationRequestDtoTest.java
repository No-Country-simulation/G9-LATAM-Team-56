package com.finance_ia.api.dto.recommendation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;

class RecommendationRequestDtoTest {

    @Test
    void shouldCreateValidRequestDto() {

        List<TopCategoryDto> categories = List.of(
                new TopCategoryDto(
                        ExpenseCategory.RESTAURANT,
                        1
                ),
                new TopCategoryDto(
                        ExpenseCategory.TRANSPORTE,
                        2
                ),
                new TopCategoryDto(
                        ExpenseCategory.VIVIENDA,
                        3
                )
        );

        RecommendationRequestDto request =
                new RecommendationRequestDto(
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
    void shouldRejectNullProfile() {

        List<TopCategoryDto> categories = List.of(
                new TopCategoryDto(
                        ExpenseCategory.RESTAURANT,
                        1
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequestDto(
                        null,
                        categories
                )
        );
    }

    @Test
    void shouldRejectNullTopCategories() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequestDto(
                        FinancialProfile.RIESGO,
                        null
                )
        );
    }

    @Test
    void shouldRejectEmptyTopCategories() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RecommendationRequestDto(
                        FinancialProfile.RIESGO,
                        List.of()
                )
        );
    }

    @Test
    void shouldProtectTopCategoriesFromExternalModification() {

        List<TopCategoryDto> categories = new ArrayList<>();

        categories.add(
                new TopCategoryDto(
                        ExpenseCategory.RESTAURANT,
                        1
                )
        );

        RecommendationRequestDto request =
                new RecommendationRequestDto(
                        FinancialProfile.RIESGO,
                        categories
                );

        categories.add(
                new TopCategoryDto(
                        ExpenseCategory.TRANSPORTE,
                        2
                )
        );

        assertEquals(
                1,
                request.topCategories().size()
        );
    }
}