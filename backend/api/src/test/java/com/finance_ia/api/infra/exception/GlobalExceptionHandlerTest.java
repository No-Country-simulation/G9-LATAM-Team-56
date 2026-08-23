package com.finance_ia.api.infra.exception;

import com.finance_ia.api.controller.AnalisisFinancieroController;
import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.service.AnalisisFinancieroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalisisFinancieroController.class)
@Import({
        GlobalExceptionHandler.class,
        GlobalExceptionHandlerTest.TestConfig.class
})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalisisFinancieroService service;

    @Test
    void debeConvertirValidationExceptionEnRespuesta400() throws Exception {

        ValidationException exception =
                new ValidationException(
                        List.of(
                                new ErrorDetail(
                                        "ingreso_mensual",
                                        "El ingreso mensual debe ser mayor a 0.",
                                        null
                                ),
                                new ErrorDetail(
                                        "nivel_endeudamiento",
                                        "El nivel de endeudamiento debe estar entre 0 y 100.",
                                        null
                                )
                        )
                );

        when(service.analizar(any(AnalisisFinancieroRequest.class)))
                .thenThrow(exception);

        String requestJson = """
                {
                    "ingreso_mensual": 5000,
                    "nivel_endeudamiento": 50,
                    "frecuencia_ahorro": "alta",
                    "transacciones": [
                        {
                            "descripcion": "Compra supermercado",
                            "valor": 100
                        }
                    ]
                }
                """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Se encontraron errores de validación."))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(2))

                .andExpect(jsonPath("$.errors[0].field")
                        .value("ingreso_mensual"))
                .andExpect(jsonPath("$.errors[0].message")
                        .value(
                                "El ingreso mensual debe ser mayor a 0."
                        ))
                .andExpect(jsonPath("$.errors[0].row").doesNotExist())

                .andExpect(jsonPath("$.errors[1].field")
                        .value("nivel_endeudamiento"))
                .andExpect(jsonPath("$.errors[1].message")
                        .value(
                                "El nivel de endeudamiento debe estar entre 0 y 100."
                        ))
                .andExpect(jsonPath("$.errors[1].row").doesNotExist());
    }

    @Test
    void debeConvertirIllegalArgumentExceptionEnRespuesta400()
            throws Exception {

        when(service.analizar(any(AnalisisFinancieroRequest.class)))
                .thenThrow(
                        new IllegalArgumentException(
                                "El nombre del usuario es obligatorio"
                        )
                );

        String requestJson = """
            {
                "ingreso_mensual": 5000,
                "nivel_endeudamiento": 50,
                "frecuencia_ahorro": "alta",
                "transacciones": [
                        {
                            "descripcion": "Compra supermercado",
                            "valor": 100
                        }
                    ]
            }
            """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value("El nombre del usuario es obligatorio"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void debeConvertirExcepcionInternaEnRespuesta500()
            throws Exception {

        when(service.analizar(any(AnalisisFinancieroRequest.class)))
                .thenThrow(
                        new RuntimeException("Error interno inesperado")
                );

        String requestJson = """
        {
            "ingreso_mensual": 5000,
            "nivel_endeudamiento": 50,
            "frecuencia_ahorro": "alta",
            "transacciones": [
                {
                    "descripcion": "Compra supermercado",
                    "valor": 100
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error")
                        .value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "Ocurrió un error interno en el servidor."
                        ))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void debeConvertirHttpMessageNotReadableEnRespuesta400()
            throws Exception {

        String requestJson = """
            {
                "ingreso_mensual": "abc",
                "nivel_endeudamiento": 50,
                "frecuencia_ahorro": "alta",
                "transacciones": []
            }
            """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("MALFORMED_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "La solicitud contiene datos con formato inválido."
                        ))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void debeConvertirCsvValidationExceptionEnRespuesta400()
            throws Exception {

        CsvValidationException exception =
                new CsvValidationException(
                        List.of(
                                new ErrorDetail(
                                        "valor",
                                        "El valor debe ser mayor que 0.",
                                        4
                                ),
                                new ErrorDetail(
                                        "fecha",
                                        "La fecha no tiene un formato válido.",
                                        7
                                )
                        )
                );

        when(service.analizar(any(AnalisisFinancieroRequest.class)))
                .thenThrow(exception);

        String requestJson = """
            {
                "ingreso_mensual": 5000,
                "nivel_endeudamiento": 50,
                "frecuencia_ahorro": "alta",
                "transacciones": [
                        {
                            "descripcion": "Compra supermercado",
                            "valor": 100
                        }
                    ]
            }
            """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("CSV_VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "El archivo CSV contiene datos inválidos."
                        ))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(2))

                .andExpect(jsonPath("$.errors[0].field")
                        .value("valor"))
                .andExpect(jsonPath("$.errors[0].message")
                        .value("El valor debe ser mayor que 0."))
                .andExpect(jsonPath("$.errors[0].row")
                        .value(4))

                .andExpect(jsonPath("$.errors[1].field")
                        .value("fecha"))
                .andExpect(jsonPath("$.errors[1].message")
                        .value("La fecha no tiene un formato válido."))
                .andExpect(jsonPath("$.errors[1].row")
                        .value(7));
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        AnalisisFinancieroService analisisFinancieroService() {
            return mock(AnalisisFinancieroService.class);
        }
    }
}