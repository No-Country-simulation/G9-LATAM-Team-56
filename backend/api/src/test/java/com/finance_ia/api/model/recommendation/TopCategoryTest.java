package com.finance_ia.api.model.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TopCategoryTest {

    @Test
    @DisplayName("Debe crear una instancia válida de TopCategory")
    void shouldCreateValidTopCategory() {

        TopCategory topCategory = new TopCategory(
                ExpenseCategory.RESTAURANT,
                1
        );

        assertEquals(
                ExpenseCategory.RESTAURANT,
                topCategory.category()
        );

        assertEquals(
                1,
                topCategory.position()
        );
    }

    @Test
    @DisplayName("Debe rechazar una categoría nula")
    void shouldRejectNullCategory() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategory(null, 1)
        );
    }

    @Test
    @DisplayName("Debe rechazar una posición igual a cero")
    void shouldRejectInvalidPosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategory(
                        ExpenseCategory.RESTAURANT,
                        0
                )
        );
    }

    @Test
    @DisplayName("Debe rechazar una posición negativa")
    void shouldRejectNegativePosition() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TopCategory(
                        ExpenseCategory.RESTAURANT,
                        -1
                )
        );
    }

    @Test
    @DisplayName("Debe aceptar una posición mayor a uno")
    void shouldAcceptPositionGreaterThanOne() {

        TopCategory topCategory = new TopCategory(
                ExpenseCategory.TRANSPORTE,
                3
        );

        assertEquals(
                3,
                topCategory.position()
        );
    }
}