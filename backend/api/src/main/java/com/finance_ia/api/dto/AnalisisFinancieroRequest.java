package com.finance_ia.api.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AnalisisFinancieroRequest {

    @NotNull(message = "El ingreso mensual es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El ingreso mensual debe ser mayor a 0.")
    private Double ingreso_mensual;

    @NotNull(message = "El nivel de endeudamiento es obligatorio.")
    @DecimalMin(
            value = "0.0",
            message = "El nivel de endeudamiento debe estar entre 0 y 100."
    )
    @DecimalMax(
            value = "100.0",
            message = "El nivel de endeudamiento debe estar entre 0 y 100."
    )
    private Double nivel_endeudamiento;

    @NotNull(message = "La frecuencia de ahorro es obligatoria.")
    private String frecuencia_ahorro;

    @NotEmpty(message = "Debe existir al menos una transacción.")
    @Valid
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
