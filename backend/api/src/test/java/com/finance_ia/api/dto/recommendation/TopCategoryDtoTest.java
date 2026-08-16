package com.finance_ia.api.dto.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.finance_ia.api.model.recommendation.ExpenseCategory;

class TopCategoryDtoTest {

    @Test
    void shouldCreateValidTopCategoryDto() {

        TopCategoryDto dto =
                new TopCategoryDto(
                        ExpenseCategory.RESTAURANT,
                        1
                );

        assertEquals(
                ExpenseCategory.RESTAURANT,
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
                        ExpenseCategory.RESTAURANT,
                        0
                )
        );
    }

    @Test
    void shouldRejectNegativePosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategoryDto(
                        ExpenseCategory.RESTAURANT,
                        -1
                )
        );
    }
}