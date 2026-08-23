package com.finance_ia.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "analisis_financiero")
public class AnalisisFinancieroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental (1, 2, 3...)
    private Long id;

    private String usuarioNombre;
    private double ingresoMensual;
    private double nivelEndeudamiento;
    private String frecuenciaAhorro;
    private String divisa;
    private String perfilFinanciero;
    private double probabilidad;
    private Double saldoTotal;
    private Double gastoTotal;

    @OneToMany(mappedBy = "analisisFinanciero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaccionEntity> transacciones;

    @OneToMany(
            mappedBy = "analisisFinanciero",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecomendacionEntity> recomendaciones = new ArrayList<>();

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public double getIngresoMensual() {
        return ingresoMensual;
    }
    public void setIngresoMensual(double ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public double getNivelEndeudamiento() {
        return nivelEndeudamiento;
    }
    public void setNivelEndeudamiento(double nivelEndeudamiento) {
        this.nivelEndeudamiento = nivelEndeudamiento;
    }

    public String getFrecuenciaAhorro() {
        return frecuenciaAhorro;
    }
    public void setFrecuenciaAhorro(String frecuenciaAhorro) {
        this.frecuenciaAhorro = frecuenciaAhorro;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getPerfilFinanciero() {
        return perfilFinanciero;
    }
    public void setPerfilFinanciero(String perfilFinanciero) {
        this.perfilFinanciero = perfilFinanciero;
    }

    public double getProbabilidad() {
        return probabilidad;
    }
    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public List<TransaccionEntity> getTransacciones() {
        return transacciones;
    }
    public void setTransacciones(List<TransaccionEntity> transacciones) {
        this.transacciones = transacciones;
    }

    public Double getSaldoTotal() { return saldoTotal; }
    public void setSaldoTotal(Double saldoTotal) { this.saldoTotal = saldoTotal; }

    public Double getGastoTotal() { return gastoTotal; }
    public void setGastoTotal(Double gastoTotal) { this.gastoTotal = gastoTotal; }

    public List<RecomendacionEntity> getRecomendaciones() {return recomendaciones;}
    public void setRecomendaciones(List<RecomendacionEntity> recomendaciones) {this.recomendaciones = recomendaciones;}
}