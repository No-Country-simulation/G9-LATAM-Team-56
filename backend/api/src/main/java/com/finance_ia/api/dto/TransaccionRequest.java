package com.finance_ia.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(
        name = "TransaccionRequest",
        description = "Datos de una transacción financiera que será analizada."
)
public class TransaccionRequest {

    @Schema(
            description = "Descripción de la transacción",
            example = "Supermercado"
    )
    @NotBlank(message = "La descripción de la transacción es obligatoria.")
    private String descripcion;

    @Schema(
            description = "Valor monetario de la transacción",
            example = "420.0",
            minimum = "0"
    )
    @NotNull(message = "El valor de la transacción es obligatorio.")
    @Positive(message = "El valor de la transacción debe ser mayor que 0.")
    private Double valor;

    @Schema(
            description = "Fecha de la transacción. Es utilizada principalmente durante el procesamiento de archivos CSV.",
            example = "15/08/2026",
            format = "date"
    )
    @JsonDeserialize(using = LocalDateIgnoreInvalidDeserializer.class)
    private LocalDate fecha;

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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}