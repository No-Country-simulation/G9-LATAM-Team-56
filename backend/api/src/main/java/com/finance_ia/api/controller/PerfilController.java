package com.finance_ia.api.controller;

import com.finance_ia.api.model.AnalisisFinancieroEntity;
import com.finance_ia.api.model.UsuarioEntity;
import com.finance_ia.api.repository.AnalisisFinancieroRepository;
import com.finance_ia.api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
    @GetMapping("/datos")
    public ResponseEntity<?> obtenerDatosPerfil(@RequestParam("email") String email) {
        // Buscamos primero al usuario por su correo para asegurarnos que exista y obtener su nombre
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Metodo que actualiza el registro (nunca se duplica)
        Optional<AnalisisFinancieroEntity> analisisOpt = analisisFinancieroRepository.findByUsuarioNombre(usuario.getNombre());

        Map<String, Object> datosFinancieros = new HashMap<>();
        datosFinancieros.put("moneda", "USD Dólar");

        // Verificamos si encontramos datos en la tabla Analisis_financiero
        if (analisisOpt.isPresent()) {
            AnalisisFinancieroEntity analisis = analisisOpt.get();
            Double ingreso = analisis.getIngresoMensual();
            Double saldo = analisis.getSaldoTotal();

            // Configuramos el formato inglés (Coma para miles, punto para decimales)
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.US);

            // Definimos la máscara del formato: #,##0.0#
            // - #,##0 indica que ponga comas en los miles.
            // - .0# garantiza que muestre al menos un decimal y hasta dos si existen.
            DecimalFormat formateador = new DecimalFormat("#,##0.0#", simbolos);

            // Formateamos el ingreso mensual y saldo total
            String ingresoFormateado = (ingreso != null) ? "$" + formateador.format(ingreso): "$0";
            String saldoFormateado = (saldo != null) ? "$" + formateador.format(saldo) : "$0";

            // Extraemos los valores reales de la base de datos y les damos formato visual
            datosFinancieros.put("ingresoMensual", ingresoFormateado);
            datosFinancieros.put("nivelEndeudamiento", analisis.getNivelEndeudamiento() + "%");
            datosFinancieros.put("saldoTotal", saldoFormateado);
        } else {
            // Si el usuario aún no ha subido ningún CSV o no hay registros, enviamos valores por defecto en 0
            datosFinancieros.put("ingresoMensual", "$0");
            datosFinancieros.put("nivelEndeudamiento", "0%");
            datosFinancieros.put("saldoTotal", "$0");
        }

        return ResponseEntity.ok(datosFinancieros);
    }
}