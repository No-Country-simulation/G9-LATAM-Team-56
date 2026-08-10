package com.finance_ia.api.service;

import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;

@Service
public class AnalisisFinancieroService {

    private final PerfilFinancieroOnnxService perfilService;
    private final ClasificadorCategoriasOnnxService categoriasService;

    public AnalisisFinancieroService(
        PerfilFinancieroOnnxService perfilService,
        ClasificadorCategoriasOnnxService categoriasService) {

    this.perfilService = perfilService;
    this.categoriasService = categoriasService;
}

    public AnalisisFinancieroResponse analizar(
            AnalisisFinancieroRequest request
    ) {
        return null;//perfilService.predecirPerfil(request);
    }

    public PerfilFinancieroResponse obtenerPerfil(PerfilFinancieroRequest request) {
        return perfilService.predecirPerfil(request);
    }
}