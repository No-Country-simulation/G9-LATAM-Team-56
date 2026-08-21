package com.finance_ia.api.controller;

import com.finance_ia.api.model.UsuarioEntity;
import com.finance_ia.api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permitir peticiones desde React (CORS)
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
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
