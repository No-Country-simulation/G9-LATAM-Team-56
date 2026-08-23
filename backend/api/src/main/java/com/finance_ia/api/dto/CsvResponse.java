package com.finance_ia.api.dto;

import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "CsvResponse",
        description = "Resultado del procesamiento de un archivo CSV financiero."
)
public class CsvResponse {

    @Schema(
            description = "Ingreso mensual declarado por el usuario.",
            example = "5000.0"
    )
    private double ingreso_mensual;

    @Schema(
            description = "Nivel de endeudamiento del usuario expresado como porcentaje.",
            example = "35.5"
    )
    private double nivel_endeudamiento;

    @Schema(
            description = "Frecuencia con la que el usuario realiza ahorros.",
            example = "mensual"
    )
    private String frecuencia_ahorro;

    @Schema(
            description = "Divisa utilizada en la información financiera.",
            example = "BOB"
    )
    private String divisa;

    @Schema(
            description = "Perfil financiero identificado para el usuario.",
            example = "AHORRADOR"
    )
    private String perfil_financiero;

    @Schema(
            description = "Probabilidad asociada al perfil financiero identificado.",
            example = "0.87"
    )
    private double probabilidad;

    @Schema(
            description = "Transacciones procesadas a partir del archivo CSV."
    )
    private List<TransaccionResponse> transacciones;

    @Schema(
            description = "Recomendaciones financieras generadas a partir del análisis."
    )
    private List<RecommendationResultDto> recomendaciones;

    public double getIngreso_mensual() {
        return ingreso_mensual;
    }

    public void setIngreso_mensual(double ingreso_mensual) {
        this.ingreso_mensual = ingreso_mensual;
    }

    public double getNivel_endeudamiento() {
        return nivel_endeudamiento;
    }

    public void setNivel_endeudamiento(double nivel_endeudamiento) {
        this.nivel_endeudamiento = nivel_endeudamiento;
    }

    public String getFrecuencia_ahorro() {
        return frecuencia_ahorro;
    }

    public void setFrecuencia_ahorro(String frecuencia_ahorro) {
        this.frecuencia_ahorro = frecuencia_ahorro;
    }

    public String getPerfil_financiero() {
        return perfil_financiero;
    }

    public void setPerfil_financiero(String perfil_financiero) {
        this.perfil_financiero = perfil_financiero;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public List<TransaccionResponse> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionResponse> transacciones) {
        this.transacciones = transacciones;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }


    public List<RecommendationResultDto> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<RecommendationResultDto> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}