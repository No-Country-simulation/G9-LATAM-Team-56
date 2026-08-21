package com.finance_ia.api.controller;

import com.finance_ia.api.dto.EstadisticasResponseDto;
import com.finance_ia.api.service.EstadisticasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*") // Permite peticiones desde React
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping
    public ResponseEntity<EstadisticasResponseDto> obtenerEstadisticas(
            @RequestParam String usuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        EstadisticasResponseDto response = estadisticasService.obtenerDatosEstadisticos(usuario, inicio, fin);
        return ResponseEntity.ok(response);
    }
}