package com.finance_ia.api.dto;

import java.util.List;
import java.util.Map;

public class AnalisisFinancieroResponse {

    private String perfil_financiero;
    private Double probabilidad;
    private Map<String, Double> resumen_gastos;
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
