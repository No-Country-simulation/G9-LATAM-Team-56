package com.finance_ia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class TransaccionResponse {

    private String descripcion;
    private Double valor;
    private String categoria;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;

    public TransaccionResponse() {
    }

    public TransaccionResponse(
            String descripcion,
            Double valor,
            String categoria,
            LocalDate fecha) {

        this.descripcion = descripcion;
        this.valor = valor;
        this.categoria = categoria;
        this.fecha = fecha;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}