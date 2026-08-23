package com.finance_ia.api.controller;

import com.finance_ia.api.infra.exception.ErrorResponse;
import com.finance_ia.api.model.AnalisisFinancieroEntity;
import com.finance_ia.api.model.UsuarioEntity;
import com.finance_ia.api.repository.AnalisisFinancieroRepository;
import com.finance_ia.api.repository.UsuarioRepository;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Tag(
        name = "06. Perfil",
        description = "Información del perfil financiero del usuario."
)
@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = "*") // Para evitar bloqueos CORS con React
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final AnalisisFinancieroRepository analisisFinancieroRepository;

    public PerfilController(UsuarioRepository usuarioRepository, AnalisisFinancieroRepository analisisFinancieroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.analisisFinancieroRepository = analisisFinancieroRepository;
    }

    // Endpoint para obtener la información financiera del usuario por su email
    @Operation(
            summary = "Consultar perfil financiero",
            description = "Obtiene los principales datos financieros asociados a un usuario mediante su correo electrónico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos del perfil obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    description = "Datos financieros del usuario."
                            ),
                            examples = @ExampleObject(
                                    name = "Perfil financiero",
                                    summary = "Datos financieros registrados",
                                    value = """
                                        {
                                          "ingresoMensual": "$4,500.00",
                                          "nivelEndeudamiento": "25.0%",
                                          "saldoTotal": "$1,200.50",
                                          "gastoTotal": "$3,299.50",
                                          "moneda": "BOB"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object"
                            ),
                            examples = @ExampleObject(
                                    name = "Usuario no encontrado",
                                    summary = "No existe un usuario con el correo proporcionado",
                                    value = """
                                        {
                                          "error": "Usuario no encontrado"
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
                                    summary = "Error inesperado al consultar el perfil",
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
    @GetMapping("/datos")
    public ResponseEntity<?> obtenerDatosPerfil(
            @Parameter(
                    description = "Correo electrónico del usuario cuyo perfil financiero se desea consultar.",
                    example = "usuario@gmail.com",
                    required = true
            )
            @RequestParam("email") String email
    ) {
        // Buscamos primero al usuario por su correo para asegurarnos que exista y obtener su nombre
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Metodo que actualiza el registro (nunca se duplica)
        Optional<AnalisisFinancieroEntity> analisisOpt = analisisFinancieroRepository.findByUsuarioNombre(usuario.getNombre());

        Map<String, Object> datosFinancieros = new HashMap<>();

        // Verificamos si encontramos datos en la tabla Analisis_financiero
        if (analisisOpt.isPresent()) {
            AnalisisFinancieroEntity analisis = analisisOpt.get();
            Double ingreso = analisis.getIngresoMensual();
            Double saldo = analisis.getSaldoTotal();
            Double gasto = analisis.getGastoTotal();

            // Configuramos el formato inglés (Coma para miles, punto para decimales)
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.US);

            // Definimos la máscara del formato: #,##0.0#
            // - #,##0 indica que ponga comas en los miles.
            // - .0# garantiza que muestre al menos un decimal y hasta dos si existen.
            DecimalFormat formateador = new DecimalFormat("#,##0.0#", simbolos);

            // Formateamos el ingreso mensual y saldo/gasto total
            String ingresoFormateado = (ingreso != null) ? "$" + formateador.format(ingreso): "$0";
            String saldoFormateado = (saldo != null) ? "$" + formateador.format(saldo): "$0";
            String gastoFormateado = (gasto != null) ? "$" + formateador.format(gasto): "$0";

            // Extraemos los valores reales de la base de datos y les damos formato visual
            datosFinancieros.put("ingresoMensual", ingresoFormateado);
            datosFinancieros.put("nivelEndeudamiento", analisis.getNivelEndeudamiento() + "%");
            datosFinancieros.put("saldoTotal", saldoFormateado);
            datosFinancieros.put("gastoTotal", gastoFormateado);
            datosFinancieros.put("moneda", analisis.getDivisa() != null ? analisis.getDivisa(): "Sin registrar");
        } else {
            // Si el usuario aún no ha subido ningún CSV o no hay registros, enviamos valores por defecto en 0
            datosFinancieros.put("ingresoMensual", "$0");
            datosFinancieros.put("nivelEndeudamiento", "0%");
            datosFinancieros.put("saldoTotal", "$0");
            datosFinancieros.put("gastoTotal", "$0");
            datosFinancieros.put("moneda", "Sin registrar");
        }

        return ResponseEntity.ok(datosFinancieros);
    }
}