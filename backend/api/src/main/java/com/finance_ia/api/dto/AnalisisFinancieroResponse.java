package com.finance_ia.api.dto;

public class AnalisisFinancieroResponse {

    private String perfil_financiero;
    private Double probabilidad;

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
}