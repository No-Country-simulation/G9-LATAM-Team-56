package com.finance_ia.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ClasificacionTransaccionesRequest {

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