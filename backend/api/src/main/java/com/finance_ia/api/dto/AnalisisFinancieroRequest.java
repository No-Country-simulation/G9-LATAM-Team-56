package com.finance_ia.api.dto;

public class AnalisisFinancieroRequest {

    private Double ingreso_mensual;
    private Double nivel_endeudamiento;
    private String frecuencia_ahorro;
    private Double gasto_total;

    public AnalisisFinancieroRequest() {
    }

    public Double getIngreso_mensual() {
        return ingreso_mensual;
    }

    public void setIngreso_mensual(Double ingreso_mensual) {
        this.ingreso_mensual = ingreso_mensual;
    }

    public Double getNivel_endeudamiento() {
        return nivel_endeudamiento;
    }

    public void setNivel_endeudamiento(Double nivel_endeudamiento) {
        this.nivel_endeudamiento = nivel_endeudamiento;
    }

    public String getFrecuencia_ahorro() {
        return frecuencia_ahorro;
    }

    public void setFrecuencia_ahorro(String frecuencia_ahorro) {
        this.frecuencia_ahorro = frecuencia_ahorro;
    }

    public Double getGasto_total() {
        return gasto_total;
    }

    public void setGasto_total(Double gasto_total) {
        this.gasto_total = gasto_total;
    }
}