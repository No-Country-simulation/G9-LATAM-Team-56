package com.finance_ia.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.dto.ClasificacionTransaccionesRequest;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;
import com.finance_ia.api.dto.TransaccionResponse;

@Service
public class AnalisisFinancieroService {

    private final PerfilFinancieroOnnxService perfilService;
    private final ClasificadorTransaccionesOnnxService transaccionesService;

    public AnalisisFinancieroService(
            PerfilFinancieroOnnxService perfilService,
            ClasificadorTransaccionesOnnxService transaccionesService) {

        this.perfilService = perfilService;
        this.transaccionesService = transaccionesService;
    }

    public AnalisisFinancieroResponse analizar(
            AnalisisFinancieroRequest request
    ) {
        // 1. Preparar request para el clasificador de transacciones 
        ClasificacionTransaccionesRequest clasificacionRequest = new ClasificacionTransaccionesRequest();
        clasificacionRequest.setTransacciones(request.getTransacciones()); // 2. Clasificar las transacciones 
        ClasificacionTransaccionesResponse clasificacionResponse = transaccionesService.clasificar(clasificacionRequest);
        // 3. Obtener las transacciones categorizadas 
        List<TransaccionResponse> transaccionesCategorizadas = clasificacionResponse.getTransacciones();
        // 4. Calcular gasto total y resumen por categoría 
        double gastoTotal = 0.0;
        Map<String, Double> resumenGastos = new HashMap<>();
        for (TransaccionResponse transaccion : transaccionesCategorizadas) {
            double valor = transaccion.getValor();
            gastoTotal += valor;
            resumenGastos.merge(transaccion.getCategoria(), valor, Double::sum);
        } // 5. Preparar request para el modelo de perfil financiero 
        PerfilFinancieroRequest perfilRequest = new PerfilFinancieroRequest();
        perfilRequest.setIngreso_mensual(request.getIngreso_mensual());
        perfilRequest.setNivel_endeudamiento(request.getNivel_endeudamiento());
        perfilRequest.setFrecuencia_ahorro(request.getFrecuencia_ahorro());
        perfilRequest.setGasto_total(gastoTotal); // 6. Ejecutar modelo de perfil financiero 
        PerfilFinancieroResponse perfilResponse = perfilService.predecirPerfil(perfilRequest);
        // 7. Construir respuesta final 
        AnalisisFinancieroResponse response = new AnalisisFinancieroResponse();
        response.setPerfil_financiero(perfilResponse.getPerfil_financiero());
        response.setProbabilidad(perfilResponse.getProbabilidad());
        response.setResumen_gastos(resumenGastos);
        // Temporalmente, hasta tener la capa de reglas de negocio 
        response.setRecomendaciones(new ArrayList<>());
        return response;
    }

    public PerfilFinancieroResponse obtenerPerfil(PerfilFinancieroRequest request) {
        return perfilService.predecirPerfil(request);
    }

    public ClasificacionTransaccionesResponse clasificarTransacciones(ClasificacionTransaccionesRequest request) {
        return transaccionesService.clasificar(request);
    }

}
