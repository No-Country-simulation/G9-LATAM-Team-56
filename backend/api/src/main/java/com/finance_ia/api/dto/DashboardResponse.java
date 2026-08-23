package com.finance_ia.api.dto;

import com.finance_ia.api.dto.recommendation.RecommendationResultDto;

import java.util.List;
import java.util.Map;

public record DashboardResponse(
        String perfil_financiero,
        Double probabilidad,
        Double ingreso_mensual,
        Double nivel_endeudamiento,
        String frecuencia_ahorro,
        String divisa,
        Double saldo_total,
        Map<String, Double> resumen_gastos,
        List<DashboardTransaccionDto> transacciones,
        List<RecommendationResultDto> recomendaciones
) {

    public DashboardResponse {
        if (perfil_financiero == null) {
            throw new IllegalArgumentException(
                    "El perfil financiero es obligatorio"
            );
        }

        if (probabilidad == null) {
            throw new IllegalArgumentException(
                    "La probabilidad es obligatoria"
            );
        }

        if (ingreso_mensual == null) {
            throw new IllegalArgumentException(
                    "El ingreso mensual es obligatorio"
            );
        }

        if (nivel_endeudamiento == null) {
            throw new IllegalArgumentException(
                    "El nivel de endeudamiento es obligatorio"
            );
        }

        if (frecuencia_ahorro == null) {
            throw new IllegalArgumentException(
                    "La frecuencia de ahorro es obligatoria"
            );

        }

        if (saldo_total == null) {
            throw new IllegalArgumentException(
                    "El saldo total es obligatorio"
            );
        }

        if (resumen_gastos == null) {
            throw new IllegalArgumentException(
                    "El resumen de gastos es obligatorio"
            );
        }

        if (transacciones == null) {
            throw new IllegalArgumentException(
                    "La lista de transacciones es obligatoria"
            );
        }

        if (recomendaciones == null) {
            throw new IllegalArgumentException(
                    "La lista de recomendaciones es obligatoria"
            );
        }

        recomendaciones = List.copyOf(recomendaciones);
    }
}
