package com.finance_ia.api.dto;

import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CsvResponse {

    private double ingreso_mensual;
    private double nivel_endeudamiento;
    private String frecuencia_ahorro;
    private String perfil_financiero;
    private double probabilidad;
    private List<TransaccionResponse> transacciones;
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

    
}