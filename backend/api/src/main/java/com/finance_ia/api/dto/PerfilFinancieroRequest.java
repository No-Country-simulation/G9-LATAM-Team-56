package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "PerfilFinancieroRequest",
        description = "Datos financieros utilizados para evaluar el perfil financiero del usuario."
)
public class PerfilFinancieroRequest {

    @Schema(
            description = "Ingreso mensual del usuario.",
            example = "4500"
    )
    @NotNull(message = "El ingreso mensual es obligatorio.")
    @DecimalMin(
            value = "0.0", inclusive = false,
            message = "El ingreso mensual debe ser mayor a 0."
    )
    private Double ingreso_mensual;

    @Schema(
            description = "Porcentaje de endeudamiento del usuario.",
            example = "25",
            minimum = "0",
            maximum = "100"
    )
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

    @Schema(
            description = "Frecuencia con la que el usuario realiza ahorros.",
            example = "MEDIA"
    )
    @NotNull(message = "La frecuencia de ahorro es obligatoria.")
    private String frecuencia_ahorro;

    @Schema(
            description = "Gasto total del usuario.",
            example = "3500",
            minimum = "0"
    )
    @NotNull(message = "El gasto total es obligatorio.")
    @DecimalMin(
            value = "0.0",
            message = "El gasto total debe ser mayor o igual a 0."
    )
    private Double gasto_total;

    public PerfilFinancieroRequest() {
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