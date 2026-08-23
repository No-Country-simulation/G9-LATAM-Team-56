package com.finance_ia.api.controller;

import com.finance_ia.api.dto.EstadisticasResponseDto;
import com.finance_ia.api.infra.exception.ErrorResponse;
import com.finance_ia.api.service.EstadisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(
        name = "05. Estadísticas",
        description = "Obtención de métricas y estadísticas financieras."
)
@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*") // Permite peticiones desde React
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @Operation(
            summary = "Consultar estadísticas financieras",
            description = "Obtiene métricas y estadísticas financieras de un usuario dentro de un período determinado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los parámetros enviados no tienen un formato válido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitud con formato inválido",
                                    summary = "Fecha con formato incorrecto",
                                    value = """
                                        {
                                          "timestamp": "2026-08-23T10:30:00",
                                          "status": 400,
                                          "error": "MALFORMED_REQUEST",
                                          "message": "La solicitud contiene datos con formato inválido.",
                                          "errors": []
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
                                    summary = "Error inesperado al obtener las estadísticas",
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
    @GetMapping
    public ResponseEntity<EstadisticasResponseDto> obtenerEstadisticas(
            @Parameter(
                    description = "Nombre del usuario cuyas estadísticas se desean consultar.",
                    example = "Ana",
                    required = true
            )
            @RequestParam String usuario,

            @Parameter(
                    description = "Fecha inicial del período de consulta.",
                    example = "2026-08-01",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @Parameter(
                    description = "Fecha final del período de consulta.",
                    example = "2026-08-23",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {

        EstadisticasResponseDto response =
                estadisticasService.obtenerDatosEstadisticos(
                        usuario,
                        inicio,
                        fin
                );

        return ResponseEntity.ok(response);
    }
}