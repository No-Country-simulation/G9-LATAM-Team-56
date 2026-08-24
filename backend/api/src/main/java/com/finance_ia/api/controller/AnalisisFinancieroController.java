package com.finance_ia.api.controller;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.dto.ClasificacionTransaccionesRequest;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;
import com.finance_ia.api.service.AnalisisFinancieroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.finance_ia.api.infra.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(
        name = "03. Análisis Financiero",
        description = "Clasificación de transacciones y evaluación del perfil financiero."
)
@RestController
@CrossOrigin(origins = "*") // Permitir peticiones desde React (CORS)
public class AnalisisFinancieroController {

    private final AnalisisFinancieroService service;

    public AnalisisFinancieroController(
            AnalisisFinancieroService service
    ) {
        this.service = service;
    }

    @Operation(
            operationId = "01_analisis",
            summary = "Realizar análisis financiero",
            description = "Analiza la información financiera del usuario y devuelve su perfil financiero, resumen de gastos y recomendaciones."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Análisis financiero realizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Error de validación",
                                    summary = "Datos de entrada inválidos",
                                    value = """
                                        {
                                          "timestamp": "2026-08-23T10:30:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Los datos enviados no son válidos.",
                                          "errors": [
                                            {
                                              "field": "ingreso_mensual",
                                              "message": "El ingreso mensual debe ser mayor a 0.",
                                              "row": null
                                            }
                                          ]
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Error interno",
                                    summary = "Excepción inesperada",
                                    value = """
                                        {
                                          "timestamp": "2026-08-23T10:30:00",
                                          "status": 500,
                                          "error": "INTERNAL_SERVER_ERROR",
                                          "message": "Ocurrió un error interno en el servidor.",
                                          "errors": []
                                        }
                                        """
                            )
                    )
            )
    })
    @PostMapping("/analisis-financiero")
    public AnalisisFinancieroResponse analisis(
            @Valid @RequestBody AnalisisFinancieroRequest request) {

        return service.analizar(request);
    }

    @Operation(
            operationId = "02_perfil",
            summary = "Obtener perfil financiero",
            description = """
            Evalúa el perfil financiero del usuario utilizando
            indicadores financieros como ingreso mensual,
            nivel de endeudamiento, frecuencia de ahorro
            y gasto total.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil financiero calculado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = PerfilFinancieroResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            ),
                            examples = @ExampleObject(
                                    name = "Error de validación",
                                    summary = "Datos de entrada inválidos",
                                    value = """
                                    {
                                      "timestamp": "2026-08-23T10:30:00",
                                      "status": 400,
                                      "error": "VALIDATION_ERROR",
                                      "message": "Los datos enviados no son válidos.",
                                      "errors": [
                                        {
                                          "field": "ingreso_mensual",
                                          "message": "El ingreso mensual debe ser mayor a 0.",
                                          "row": null
                                        }
                                      ]
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            ),
                            examples = @ExampleObject(
                                    name = "Error interno",
                                    summary = "Excepción inesperada",
                                    value = """
                                    {
                                      "timestamp": "2026-08-23T10:30:00",
                                      "status": 500,
                                      "error": "INTERNAL_SERVER_ERROR",
                                      "message": "Ocurrió un error interno en el servidor.",
                                      "errors": []
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/perfil-financiero")
    public PerfilFinancieroResponse perfil(
            @Valid @RequestBody PerfilFinancieroRequest request) {

        return service.obtenerPerfil(request);
    }

    @Operation(
            operationId = "03_clasificacion",
            summary = "Clasificar transacciones",
            description = """
            Clasifica automáticamente las transacciones
            financieras en categorías como alimentación,
            transporte, salud, educación, vivienda
            y otras categorías soportadas por el modelo.
            """

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transacciones clasificadas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ClasificacionTransaccionesResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            ),
                            examples = @ExampleObject(
                                    name = "Error de validación",
                                    summary = "Datos de entrada inválidos",
                                    value = """
                                    {
                                      "timestamp": "2026-08-23T10:30:00",
                                      "status": 400,
                                      "error": "VALIDATION_ERROR",
                                      "message": "Los datos enviados no son válidos.",
                                      "errors": [
                                        {
                                          "field": "transacciones",
                                          "message": "Debe existir al menos una transacción.",
                                          "row": null
                                        }
                                      ]
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            ),
                            examples = @ExampleObject(
                                    name = "Error interno",
                                    summary = "Excepción inesperada",
                                    value = """
                                    {
                                      "timestamp": "2026-08-23T10:30:00",
                                      "status": 500,
                                      "error": "INTERNAL_SERVER_ERROR",
                                      "message": "Ocurrió un error interno en el servidor.",
                                      "errors": []
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/clasificacion-transacciones")
    public ClasificacionTransaccionesResponse clasificarTransacciones(
            @Valid @RequestBody ClasificacionTransaccionesRequest request) {

        return service.clasificarTransacciones(request);
    }

}
