package com.finance_ia.api.dto;

import java.util.List;

public class ClasificacionTransaccionesResponse {

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