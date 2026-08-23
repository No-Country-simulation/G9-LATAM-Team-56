package com.finance_ia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "ClasificacionTransaccionesResponse",
        description = "Resultado de la clasificación de las transacciones financieras."
)
public class ClasificacionTransaccionesResponse {

    @Schema(
            description = "Lista de transacciones con la categoría asignada por el modelo."
    )
    private List<TransaccionResponse> transacciones;

    public ClasificacionTransaccionesResponse() {
    }

    public ClasificacionTransaccionesResponse(
            List<TransaccionResponse> transacciones) {

        this.transacciones = transacciones;
    }

    public List<TransaccionResponse> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(
            List<TransaccionResponse> transacciones) {

        this.transacciones = transacciones;
    }
}