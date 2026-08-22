package com.finance_ia.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class TransaccionRequest {

        @NotBlank(message = "La descripción de la transacción es obligatoria.")
        private String descripcion;

        @NotNull(message = "El valor de la transacción es obligatorio.")
        @Positive(message = "El valor de la transacción debe ser mayor que 0.")
        private Double valor;

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