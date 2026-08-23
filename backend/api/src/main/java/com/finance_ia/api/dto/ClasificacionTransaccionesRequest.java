package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(
        name = "ClasificacionTransaccionesRequest",
        description = "Solicitud para clasificar un conjunto de transacciones financieras."
)
public class ClasificacionTransaccionesRequest {

    @Schema(
            description = "Lista de transacciones que serán clasificadas."
    )
    @NotEmpty(message = "Debe existir al menos una transacción.")
    @Valid
    private List<TransaccionRequest> transacciones;

    public ClasificacionTransaccionesRequest() {
    }

    public List<TransaccionRequest> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionRequest> transacciones) {
        this.transacciones = transacciones;
    }
}