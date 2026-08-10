package com.finance_ia.api.dto.recommendation;

import com.finance_ia.api.model.recommendation.ExpenseCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopCategoryDtoTest {

    @Test
    void shouldCreateValidTopCategoryDto() {

        TopCategoryDto dto =
                new TopCategoryDto(
                        ExpenseCategory.RESTAURANTE,
                        1
                );

        assertEquals(
                ExpenseCategory.RESTAURANTE,
                dto.category()
        );

        assertEquals(
                1,
                dto.position()
        );
    }

    @Test
    void shouldRejectNullCategory() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategoryDto(
                        null,
                        1
                )
        );
    }

    @Test
    void shouldRejectZeroPosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategoryDto(
                        ExpenseCategory.RESTAURANTE,
                        0
                )
        );
    }

    @Test
    void shouldRejectNegativePosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategoryDto(
                        ExpenseCategory.RESTAURANTE,
                        -1
                )
        );
    }
}