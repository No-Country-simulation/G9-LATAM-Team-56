package com.finance_ia.api.controller;

import com.finance_ia.api.dto.CsvResponse;
import com.finance_ia.api.service.CsvService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finance_ia.api.infra.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(
        name = "02. Importación CSV",
        description = "Carga y procesamiento de archivos CSV con información financiera."
)
@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "*")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @Operation(
            summary = "Importar archivo CSV",
            description = "Carga un archivo CSV con información financiera, procesa sus transacciones y almacena la información asociada al usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Archivo CSV procesado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El archivo o los datos enviados no son válidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Error de validación CSV",
                                    summary = "Archivo CSV con datos inválidos",
                                    value = """
                                        {
                                          "timestamp": "2026-08-23T10:30:00",
                                          "status": 400,
                                          "error": "CSV_VALIDATION_ERROR",
                                          "message": "El archivo contiene datos inválidos.",
                                          "errors": [
                                            {
                                              "field": "valor",
                                              "message": "El valor de la transacción debe ser mayor que 0.",
                                              "row": 5
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
                                    summary = "Excepción inesperada durante el procesamiento",
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
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CsvResponse> subirCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("usuario") String usuario
    ) {
        CsvResponse response = csvService.analizarYGuardarCsv(file, usuario);
        return ResponseEntity.ok(response);
    }
}
