package com.finance_ia.api.dto;

public class TransaccionResponse {

    private String descripcion;
    private Double valor;
    private String categoria;
    private String fecha;

    public TransaccionResponse() {
    }

    public TransaccionResponse(
            String descripcion,
            Double valor,
            String categoria) {

        this.descripcion = descripcion;
        this.valor = valor;
        this.categoria = categoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}