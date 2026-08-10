package com.finance_ia.api.dto;

import java.util.List;

public class AnalisisFinancieroRequest {

    private Double ingreso_mensual;
    private Double nivel_endeudamiento;
    private String frecuencia_ahorro;
    private List<TransaccionRequest> transacciones;

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

    public List<TransaccionRequest> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionRequest> transacciones) {
        this.transacciones = transacciones;
    }

}
