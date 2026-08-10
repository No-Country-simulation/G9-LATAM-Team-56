package com.finance_ia.api.dto;

import java.util.List;

public class ClasificacionTransaccionesRequest {

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