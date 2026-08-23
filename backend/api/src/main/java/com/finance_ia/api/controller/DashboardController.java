package com.finance_ia.api.controller;

import com.finance_ia.api.dto.DashboardResponse;
import com.finance_ia.api.infra.exception.ErrorResponse;
import com.finance_ia.api.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "04. Dashboard",
        description = "Consulta de indicadores financieros y resumen general."
)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        if (dashboardService == null) {
            throw new IllegalArgumentException(
                    "El servicio del dashboard es obligatorio"
            );
        }

        this.dashboardService = dashboardService;
    }

    @Operation(
            summary = "Consultar dashboard financiero",
            description = "Obtiene los principales indicadores financieros, resumen de gastos, transacciones y recomendaciones asociadas a un usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard obtenido correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud no es válida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Solicitud inválida",
                                    summary = "Parámetro de usuario inválido",
                                    value = """
                                        {
                                          "timestamp": "2026-08-23T10:30:00",
                                          "status": 400,
                                          "error": "BAD_REQUEST",
                                          "message": "El nombre de usuario no es válido.",
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
                                    summary = "Error inesperado al obtener el dashboard",
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
    @GetMapping("/{usuarioNombre}")
    public ResponseEntity<DashboardResponse> obtenerDashboard(
            @Parameter(
                    description = "Nombre del usuario cuyo dashboard financiero se desea consultar.",
                    example = "Ana",
                    required = true
            )
            @PathVariable String usuarioNombre
    ) {

        DashboardResponse response =
                dashboardService.obtenerDashboard(usuarioNombre);

        return ResponseEntity.ok(response);
    }
}
