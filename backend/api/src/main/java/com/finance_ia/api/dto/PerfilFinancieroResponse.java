package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "PerfilFinancieroResponse",
        description = "Resultado de la evaluación del perfil financiero del usuario."
)
public class PerfilFinancieroResponse {

    @Schema(
            description = "Perfil financiero asignado al usuario.",
            example = "En observacion"
    )
    private String perfil_financiero;

    @Schema(
            description = "Probabilidad asociada al perfil financiero identificado.",
            example = "0.82"
    )
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