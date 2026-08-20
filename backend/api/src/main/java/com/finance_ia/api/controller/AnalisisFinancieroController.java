package com.finance_ia.api.controller;

import jakarta.validation.Valid;
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

@RestController
public class AnalisisFinancieroController {

    private final AnalisisFinancieroService service;

    public AnalisisFinancieroController(
            AnalisisFinancieroService service
    ) {
        this.service = service;
    }

    @PostMapping("/analisis-financiero")
    public AnalisisFinancieroResponse analisis(
            @Valid @RequestBody AnalisisFinancieroRequest request) {

        return service.analizar(request);
    }

    @PostMapping("/perfil-financiero")
    public PerfilFinancieroResponse perfil(
            @Valid @RequestBody PerfilFinancieroRequest request) {

        return service.obtenerPerfil(request);
    }

    @PostMapping("/clasificacion-transacciones")
    public ClasificacionTransaccionesResponse clasificarTransacciones(
            @Valid @RequestBody ClasificacionTransaccionesRequest request) {

        return service.clasificarTransacciones(request);
    }

}
