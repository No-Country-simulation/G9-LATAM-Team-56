package com.finance_ia.api.service;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.PerfilFinancieroResponse;
import com.finance_ia.api.dto.TransaccionRequest;
import com.finance_ia.api.dto.TransaccionResponse;
import com.finance_ia.api.infra.exception.ValidationException;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.service.recommendation.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalisisFinancieroServiceTest {

    private PerfilFinancieroOnnxService perfilService;
    private ClasificadorTransaccionesOnnxService transaccionesService;
    private RecommendationService recommendationService;
    private FinancialValidationService validationService;

    private AnalisisFinancieroService service;
    private PerfilFinancieroMapper perfilMapper;

    @BeforeEach
    void setUp() {

        perfilService = mock(PerfilFinancieroOnnxService.class);
        transaccionesService = mock(
                ClasificadorTransaccionesOnnxService.class
        );
        recommendationService = mock(RecommendationService.class);
        validationService = new FinancialValidationService();
        perfilMapper = new PerfilFinancieroMapper();

        service = new AnalisisFinancieroService(
                perfilService,
                transaccionesService,
                recommendationService,
                validationService,
                perfilMapper
        );
    }

    // =========================================================
    // VALIDACIÓN
    // =========================================================

    @Test
    void debeRechazarRequestNullAntesDeProcesar() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analizar(null)
        );

        verifyNoInteractions(
                perfilService,
                transaccionesService,
                recommendationService
        );
    }

    @Test
    void debeRechazarRequestConDatosFinancierosInvalidos() {

        AnalisisFinancieroRequest request =
                crearRequest(
                        -100.0,
                        150.0,
                        "mensual",
                        List.of(
                                crearTransaccion(
                                        "Supermercado",
                                        100.0,
                                        LocalDate.of(2026, 8, 19)
                                )
                        )
                );

        assertThrows(
                ValidationException.class,
                () -> service.analizar(request)
        );

        verifyNoInteractions(
                perfilService,
                transaccionesService,
                recommendationService
        );
    }

    @Test
    void debeRechazarRequestConTransaccionesInvalidas() {

        AnalisisFinancieroRequest request =
                crearRequest(
                        5000.0,
                        50.0,
                        "alta",
                        List.of(
                                crearTransaccion(
                                        "Supermercado",
                                        -100.0,
                                        LocalDate.of(2026, 8, 19)
                                )
                        )
                );

        assertThrows(
                ValidationException.class,
                () -> service.analizar(request)
        );

        verifyNoInteractions(
                perfilService,
                transaccionesService,
                recommendationService
        );
    }

    @Test
    void debeRechazarRequestSinTransacciones() {

        AnalisisFinancieroRequest request =
                crearRequest(
                        5000.0,
                        50.0,
                        "alta",
                        List.of()
                );

        assertThrows(
                ValidationException.class,
                () -> service.analizar(request)
        );

        verifyNoInteractions(
                perfilService,
                transaccionesService,
                recommendationService
        );
    }

    // =========================================================
    // FLUJO VÁLIDO
    // =========================================================

    @Test
    void debeContinuarFlujoCuandoLosDatosSonValidos() {

        TransaccionRequest transaccion =
                crearTransaccion(
                        "Supermercado",
                        100.0,
                        LocalDate.of(2026, 8, 19)
                );

        AnalisisFinancieroRequest request =
                crearRequest(
                        5000.0,
                        50.0,
                        "alta",
                        List.of(transaccion)
                );

        TransaccionResponse transaccionResponse =
                new TransaccionResponse();

        transaccionResponse.setDescripcion("Supermercado");
        transaccionResponse.setValor(100.0);
        transaccionResponse.setCategoria("ALIMENTACION");

        ClasificacionTransaccionesResponse clasificacionResponse =
                new ClasificacionTransaccionesResponse();

        clasificacionResponse.setTransacciones(
                List.of(transaccionResponse)
        );

        when(transaccionesService.clasificar(any()))
                .thenReturn(clasificacionResponse);

        PerfilFinancieroResponse perfilResponse =
                new PerfilFinancieroResponse();

        perfilResponse.setPerfil_financiero("SALUDABLE");
        perfilResponse.setProbabilidad(0.95);

        when(perfilService.predecirPerfil(any()))
                .thenReturn(perfilResponse);

        RecommendationResponse recommendationResponse =
                new RecommendationResponse(
                        List.of()
                );

        when(
                recommendationService.generateRecommendations(any())
        ).thenReturn(recommendationResponse);

        AnalisisFinancieroResponse response =
                service.analizar(request);

        assertNotNull(response);

        verify(transaccionesService, times(1))
                .clasificar(any());

        verify(perfilService, times(1))
                .predecirPerfil(any());

        verify(recommendationService, times(1))
                .generateRecommendations(any());
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private AnalisisFinancieroRequest crearRequest(
            Double ingresoMensual,
            Double nivelEndeudamiento,
            String frecuenciaAhorro,
            List<TransaccionRequest> transacciones
    ) {

        AnalisisFinancieroRequest request =
                new AnalisisFinancieroRequest();

        request.setIngreso_mensual(ingresoMensual);
        request.setNivel_endeudamiento(nivelEndeudamiento);
        request.setFrecuencia_ahorro(frecuenciaAhorro);
        request.setTransacciones(transacciones);

        return request;
    }

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
}