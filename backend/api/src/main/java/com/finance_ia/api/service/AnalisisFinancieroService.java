package com.finance_ia.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finance_ia.api.dto.recommendation.RecommendationResult;
import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.dto.ClasificacionTransaccionesRequest;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;
import com.finance_ia.api.dto.TransaccionResponse;
import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.model.recommendation.TopCategory;
import com.finance_ia.api.service.recommendation.RecommendationService;

@Service
public class AnalisisFinancieroService {

    private final PerfilFinancieroOnnxService perfilService;
    private final ClasificadorTransaccionesOnnxService transaccionesService;
    private final RecommendationService recommendationService;
    private final FinancialValidationService validationService;
    private final PerfilFinancieroMapper perfilMapper;

    public AnalisisFinancieroService(
        PerfilFinancieroOnnxService perfilService,
        ClasificadorTransaccionesOnnxService transaccionesService,
        RecommendationService recommendationService,
        FinancialValidationService validationService,
        PerfilFinancieroMapper perfilMapper) {

        this.perfilService = perfilService;
        this.transaccionesService = transaccionesService;
        this.recommendationService = recommendationService;
        this.validationService = validationService;
        this.perfilMapper = perfilMapper;
    }

    public AnalisisFinancieroResponse analizar(
            AnalisisFinancieroRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "La solicitud es obligatoria."
            );
        }
        // Validación antes de ejecutar cualquier modelo
        validationService.validarDatosFinancieros(
                request.getIngreso_mensual(),
                request.getNivel_endeudamiento(),
                request.getFrecuencia_ahorro()
        );

        validationService.validarTransacciones(
                request.getTransacciones()
        );

        // 1. Preparar request para el clasificador de transacciones
        ClasificacionTransaccionesRequest clasificacionRequest
                = new ClasificacionTransaccionesRequest();

        clasificacionRequest.setTransacciones(request.getTransacciones());

        // 2. Clasificar las transacciones
        ClasificacionTransaccionesResponse clasificacionResponse
                = transaccionesService.clasificar(clasificacionRequest);

        // 3. Obtener las transacciones categorizadas
        List<TransaccionResponse> transaccionesCategorizadas
                = clasificacionResponse.getTransacciones();

        // 4. Calcular gasto total y resumen por categoría
        double gastoTotal = 0.0;

        Map<String, Double> resumenGastos = new HashMap<>();

        for (TransaccionResponse transaccion : transaccionesCategorizadas) {

            double valor = transaccion.getValor();

            gastoTotal += valor;

            resumenGastos.merge(
                    transaccion.getCategoria(),
                    valor,
                    Double::sum
            );
        }

        // 5. Preparar request para el modelo de perfil financiero
        PerfilFinancieroRequest perfilRequest
                = new PerfilFinancieroRequest();

        perfilRequest.setIngreso_mensual(
                request.getIngreso_mensual()
        );

        perfilRequest.setNivel_endeudamiento(
                request.getNivel_endeudamiento()
        );

        perfilRequest.setFrecuencia_ahorro(
                request.getFrecuencia_ahorro()
        );

        perfilRequest.setGasto_total(gastoTotal);

        // 6. Ejecutar modelo de perfil financiero
        PerfilFinancieroResponse perfilResponse
                = perfilService.predecirPerfil(perfilRequest);

        RecommendationResponse recommendationResponse =
                generarRecomendaciones(
                        perfilResponse.getPerfil_financiero(),
                        resumenGastos
                );

        // 10. Construir respuesta final
        AnalisisFinancieroResponse response
                = new AnalisisFinancieroResponse();

        response.setPerfil_financiero(
            perfilMapper.toResponse(perfilResponse.getPerfil_financiero())
        );

        response.setProbabilidad(
                perfilResponse.getProbabilidad()
        );

        response.setResumen_gastos(
                resumenGastos
        );

        response.setRecomendaciones(
                recommendationResponse.recommendations()
                        .stream()
                        .map(RecommendationResult::recommendation)
                        .toList()
        );

        return response;
    }

    private List<TopCategory> construirTopCategorias(
            Map<String, Double> resumenGastos
    ) {

        List<TopCategory> topCategories = new ArrayList<>();

        resumenGastos.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<String, Double>comparingByValue()
                                .reversed()
                )
                .forEachOrdered(entry -> {

                    ExpenseCategory category
                            = convertirCategoria(entry.getKey());

                    int position = topCategories.size() + 1;

                    topCategories.add(
                            new TopCategory(
                                    category,
                                    position
                            )
                    );
                });

        return topCategories;
    }

    private ExpenseCategory convertirCategoria(String categoria) {

        return ExpenseCategory.valueOf(
                categoria
                        .trim()
                        .toUpperCase()
                        .replace("Ó", "O")
                        .replace("É", "E")
                        .replace("Í", "I")
                        .replace("Ú", "U")
                        .replace("Á", "A")
        );
    }

    private FinancialProfile convertirPerfil(String perfil) {

        return switch (perfil
                .trim()
                .toUpperCase()
                .replace("Ó", "O")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ú", "U")
                .replace("Á", "A")) {
            case "SALUDABLE" ->
                FinancialProfile.SALUDABLE;

            case "EN OBSERVACION" ->
                FinancialProfile.EN_OBSERVACION;

            case "EN RIESGO" ->
                FinancialProfile.RIESGO;

            default ->
                throw new IllegalArgumentException(
                        "Perfil financiero desconocido: " + perfil
                );
        };
    }

    public PerfilFinancieroResponse obtenerPerfil(
            PerfilFinancieroRequest request
    ) {
        return perfilService.predecirPerfil(request);
    }

    public ClasificacionTransaccionesResponse clasificarTransacciones(
            ClasificacionTransaccionesRequest request
    ) {
        return transaccionesService.clasificar(request);
    }

    public RecommendationResponse generarRecomendaciones(
            String perfilFinanciero,
            Map<String, Double> resumenGastos
    ) {

        List<TopCategory> topCategories =
                construirTopCategorias(resumenGastos);

        RecommendationRequest recommendationRequest =
                new RecommendationRequest(
                        convertirPerfil(perfilFinanciero),
                        topCategories
                );

        return recommendationService.generateRecommendations(
                recommendationRequest
        );
    }

}
