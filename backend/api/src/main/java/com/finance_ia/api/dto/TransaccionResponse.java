package com.finance_ia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(
        name = "TransaccionResponse",
        description = "Transacción financiera con la categoría asignada durante el proceso de clasificación."
)
public class TransaccionResponse {

    @Schema(
            description = "Descripción de la transacción.",
            example = "Supermercado"
    )
    private String descripcion;

    @Schema(
            description = "Valor monetario de la transacción.",
            example = "420.0"
    )
    private Double valor;

    @Schema(
            description = "Categoría financiera asignada a la transacción.",
            example = "ALIMENTACION"
    )
    private String categoria;

    @Schema(
            description = "Fecha de la transacción.",
            example = "15/08/2026",
            type = "string"
    )
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