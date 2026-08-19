package com.finance_ia.api.service;

import com.finance_ia.api.dto.TransaccionRequest;
import com.finance_ia.api.infra.exception.ErrorDetail;
import com.finance_ia.api.infra.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinancialValidationServiceTest {

    private FinancialValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new FinancialValidationService();
    }

    // =========================================================
    // DATOS FINANCIEROS
    // =========================================================

    @Test
    void debeAceptarDatosFinancierosValidos() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "alta"
                )
        );
    }

    @Test
    void debeAceptarIngresoMensualCero() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        0.0,
                        50.0,
                        "alta"
                )
        );
    }

    @Test
    void debeRechazarIngresoMensualNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        null,
                        50.0,
                        "alta"
                )
        );

        assertError(
                exception,
                "ingreso_mensual",
                "El ingreso mensual es obligatorio."
        );
    }

    @Test
    void debeRechazarIngresoMensualNegativo() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        -1.0,
                        50.0,
                        "alta"
                )
        );

        assertError(
                exception,
                "ingreso_mensual",
                "El ingreso mensual debe ser mayor o igual a 0."
        );
    }

    @Test
    void debeRechazarNivelEndeudamientoNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        null,
                        "alta"
                )
        );

        assertError(
                exception,
                "nivel_endeudamiento",
                "El nivel de endeudamiento es obligatorio."
        );
    }

    @Test
    void debeAceptarNivelEndeudamientoCero() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        0.0,
                        "alta"
                )
        );
    }

    @Test
    void debeAceptarNivelEndeudamientoCien() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        100.0,
                        "alta"
                )
        );
    }

    @Test
    void debeRechazarNivelEndeudamientoNegativo() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        -1.0,
                        "alta"
                )
        );

        assertError(
                exception,
                "nivel_endeudamiento",
                "El nivel de endeudamiento debe estar entre 0 y 100."
        );
    }

    @Test
    void debeRechazarNivelEndeudamientoMayorA100() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        100.1,
                        "alta"
                )
        );

        assertError(
                exception,
                "nivel_endeudamiento",
                "El nivel de endeudamiento debe estar entre 0 y 100."
        );
    }

    // =========================================================
    // FRECUENCIA DE AHORRO
    // =========================================================

    @Test
    void debeAceptarFrecuenciaAlta() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "alta"
                )
        );
    }

    @Test
    void debeAceptarFrecuenciaMedia() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "media"
                )
        );
    }

    @Test
    void debeAceptarFrecuenciaBaja() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "baja"
                )
        );
    }

    @Test
    void debeNormalizarFrecuenciaEnMayusculas() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "ALTA"
                )
        );
    }

    @Test
    void debeNormalizarFrecuenciaConEspacios() {

        assertDoesNotThrow(() ->
                validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "  alta  "
                )
        );
    }

    @Test
    void debeRechazarFrecuenciaNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        null
                )
        );

        assertError(
                exception,
                "frecuencia_ahorro",
                "La frecuencia de ahorro es obligatoria."
        );
    }

    @Test
    void debeRechazarFrecuenciaVacia() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "   "
                )
        );

        assertError(
                exception,
                "frecuencia_ahorro",
                "La frecuencia de ahorro es obligatoria."
        );
    }

    @Test
    void debeRechazarFrecuenciaNoValida() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        5000.0,
                        50.0,
                        "mensual"
                )
        );

        assertError(
                exception,
                "frecuencia_ahorro",
                "La frecuencia de ahorro debe ser alta, media o baja."
        );
    }

    // =========================================================
    // TRANSACCIONES
    // =========================================================

    @Test
    void debeAceptarTransaccionValida() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        100.0,
                        LocalDate.of(2026, 8, 19)
                );

        assertDoesNotThrow(() ->
                validationService.validarTransaccion(transaccion)
        );
    }

    @Test
    void debeRechazarTransaccionNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(null)
        );

        assertError(
                exception,
                "transaccion",
                "La transacción es obligatoria."
        );
    }

    @Test
    void debeRechazarDescripcionNull() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        null,
                        100.0,
                        LocalDate.of(2026, 8, 19)
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "descripcion",
                "La descripción de la transacción es obligatoria."
        );
    }

    @Test
    void debeRechazarDescripcionVacia() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "   ",
                        100.0,
                        LocalDate.of(2026, 8, 19)
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "descripcion",
                "La descripción de la transacción es obligatoria."
        );
    }

    @Test
    void debeRechazarValorNull() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        null,
                        LocalDate.of(2026, 8, 19)
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "valor",
                "El valor de la transacción es obligatorio."
        );
    }

    @Test
    void debeRechazarValorCero() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        0.0,
                        LocalDate.of(2026, 8, 19)
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "valor",
                "El valor de la transacción debe ser mayor que 0."
        );
    }

    @Test
    void debeRechazarValorNegativo() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        -50.0,
                        LocalDate.of(2026, 8, 19)
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "valor",
                "El valor de la transacción debe ser mayor que 0."
        );
    }

    @Test
    void debeRechazarFechaNull() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        100.0,
                        null
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertError(
                exception,
                "fecha",
                "La fecha de la transacción es obligatoria."
        );
    }

    // =========================================================
    // LISTA DE TRANSACCIONES
    // =========================================================

    @Test
    void debeRechazarListaTransaccionesNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransacciones(null)
        );

        assertError(
                exception,
                "transacciones",
                "La lista de transacciones es obligatoria."
        );
    }

    @Test
    void debeRechazarListaTransaccionesVacia() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransacciones(List.of())
        );

        assertError(
                exception,
                "transacciones",
                "Debe existir al menos una transacción."
        );
    }

    @Test
    void debeAceptarListaTransaccionesValidas() {

        List<TransaccionRequest> transacciones = List.of(
                crearTransaccion(
                        "Supermercado",
                        100.0,
                        LocalDate.of(2026, 8, 19)
                ),
                crearTransaccion(
                        "Transporte",
                        50.0,
                        LocalDate.of(2026, 8, 18)
                )
        );

        assertDoesNotThrow(() ->
                validationService.validarTransacciones(transacciones)
        );
    }

    @Test
    void debeIndicarIndiceDeTransaccionConError() {

        List<TransaccionRequest> transacciones = List.of(
                crearTransaccion(
                        "Supermercado",
                        100.0,
                        LocalDate.of(2026, 8, 19)
                ),
                crearTransaccion(
                        "Transporte",
                        -50.0,
                        LocalDate.of(2026, 8, 18)
                )
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransacciones(transacciones)
        );

        assertError(
                exception,
                "transacciones[1].valor",
                "El valor de la transacción debe ser mayor que 0."
        );
    }

    // =========================================================
    // ACUMULACIÓN DE ERRORES
    // =========================================================

    @Test
    void debeAcumularTodosLosErroresDeDatosFinancieros() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarDatosFinancieros(
                        -100.0,
                        150.0,
                        "mensual"
                )
        );

        assertEquals(3, exception.getErrors().size());

        assertHasError(
                exception,
                "ingreso_mensual"
        );

        assertHasError(
                exception,
                "nivel_endeudamiento"
        );

        assertHasError(
                exception,
                "frecuencia_ahorro"
        );
    }

    @Test
    void debeAcumularTodosLosErroresDeUnaTransaccion() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "",
                        -100.0,
                        null
                );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransaccion(transaccion)
        );

        assertEquals(3, exception.getErrors().size());

        assertHasError(
                exception,
                "descripcion"
        );

        assertHasError(
                exception,
                "valor"
        );

        assertHasError(
                exception,
                "fecha"
        );
    }

    @Test
    void debeAcumularErroresDeDiferentesTransacciones() {

        List<TransaccionRequest> transacciones = List.of(
                crearTransaccion(
                        "",
                        -100.0,
                        LocalDate.of(2026, 8, 19)
                ),
                crearTransaccion(
                        "Transporte",
                        50.0,
                        null
                )
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validationService.validarTransacciones(transacciones)
        );

        assertEquals(3, exception.getErrors().size());

        assertHasError(
                exception,
                "transacciones[0].descripcion"
        );

        assertHasError(
                exception,
                "transacciones[0].valor"
        );

        assertHasError(
                exception,
                "transacciones[1].fecha"
        );
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    private TransaccionRequest crearTransaccion(
            String descripcion,
            Double valor,
            LocalDate fecha
    ) {

        TransaccionRequest transaccion =
                new TransaccionRequest();

        transaccion.setDescripcion(descripcion);
        transaccion.setValor(valor);
        transaccion.setFecha(fecha);

        return transaccion;
    }

    private void assertError(
            ValidationException exception,
            String expectedField,
            String expectedMessage
    ) {

        assertEquals(1, exception.getErrors().stream()
                .filter(error ->
                        error.field().equals(expectedField)
                                && error.message().equals(expectedMessage)
                )
                .count()
        );

        ErrorDetail error = exception.getErrors().stream()
                .filter(e -> e.field().equals(expectedField))
                .findFirst()
                .orElseThrow();

        assertNull(error.row());
    }

    private void assertHasError(
            ValidationException exception,
            String expectedField
    ) {

        assertTrue(
                exception.getErrors().stream()
                        .anyMatch(error ->
                                error.field().equals(expectedField)
                        )
        );
    }
}