package com.finance_ia.api.dto;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AnalisisFinancieroResponse",
        description = "Resultado consolidado del análisis financiero del usuario."
)
public class AnalisisFinancieroResponse {

    @Schema(
            description = "Clasificación del perfil financiero del usuario.",
            example = "En observacion"
    )
    private String perfil_financiero;

    @Schema(
            description = "Probabilidad asociada al perfil financiero identificado.",
            example = "0.82"
    )
    private Double probabilidad;

    @Schema(
            description = "Resumen de los gastos agrupados por categoría.",
            example = """
                    {
                      "alimentacion": 420.0,
                      "transporte": 300.0,
                      "entretenimiento": 40.0
                    }
                    """
    )
    private Map<String, Double> resumen_gastos;

    @Schema(
            description = "Recomendaciones financieras generadas a partir del análisis.",
            example = """
                    [
                      "Monitorear gastos recurrentes de entretenimiento",
                      "Aumentar reserva financiera mensual"
                    ]
                    """
    )
    private List<String> recomendaciones;

    public String getPerfil_financiero() {
        return perfil_financiero;
    }

    public void setPerfil_financiero(String perfil_financiero) {
        this.perfil_financiero = perfil_financiero;
    }

    public Double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public Map<String, Double> getResumen_gastos() {
        return resumen_gastos;
    }

    public void setResumen_gastos(Map<String, Double> resumen_gastos) {
        this.resumen_gastos = resumen_gastos;
    }

    public List<String> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<String> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}
