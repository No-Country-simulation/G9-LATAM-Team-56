package com.finance_ia.api.dto;

public class TransaccionRequest {

    private String descripcion;
    private Double valor;

    public TransaccionRequest() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}