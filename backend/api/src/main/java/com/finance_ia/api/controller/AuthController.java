package com.finance_ia.api.controller;

import com.finance_ia.api.infra.exception.ErrorResponse;
import com.finance_ia.api.model.UsuarioEntity;
import com.finance_ia.api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(
        name = "01. Autenticación",
        description = "Operaciones de autenticación y acceso al sistema."
)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permitir peticiones desde React (CORS)
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Endpoint para iniciar sesión
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica a un usuario mediante su correo electrónico y contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(
                                    name = "Login exitoso",
                                    summary = "Usuario autenticado correctamente",
                                    value = """
                                        {
                                          "mensaje": "¡Login exitoso!",
                                          "nombre": "Ana",
                                          "email": "usuario@gmail.com"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Correo o contraseña incorrectos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(
                                    name = "Credenciales incorrectas",
                                    summary = "El correo o la contraseña no son válidos",
                                    value = """
                                        {
                                          "error": "Correo o contraseña incorrectos"
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
                                    summary = "Error inesperado durante la autenticación",
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
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(
                                    name = "Credenciales",
                                    summary = "Credenciales de acceso",
                                    value = """
                                        {
                                          "email": "usuario@gmail.com",
                                          "password": "123456"
                                        }
                                        """
                            )
                    )
            )
            @RequestBody Map<String, String> credenciales
    ) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        // Buscamos el usuario por su correo en MySQL
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();

            // Validamos si la contraseña coincide
            if (usuario.getPassword().equals(password)) {
                // Login exitoso: Devolvemos los datos del usuario
                return ResponseEntity.ok(Map.of(
                        "mensaje", "¡Login exitoso!",
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail()
                ));
            }
        }

        // Si el correo no existe o la contraseña es incorrecta
        return ResponseEntity.status(401).body(Map.of("error", "Correo o contraseña incorrectos"));
    }
}
