package com.finance_ia.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transaccion")
public class TransaccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private double valor;
    private String categoria;
    private LocalDate fecha;

    // AGREGAR LA RELACIÓN INVERSA (ManyToOne) hacia el Análisis Financiero
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analisis_financiero_id")
    private AnalisisFinancieroEntity analisisFinanciero;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public AnalisisFinancieroEntity getAnalisisFinanciero() { return analisisFinanciero; }
    public void setAnalisisFinanciero(AnalisisFinancieroEntity analisisFinanciero) { this.analisisFinanciero = analisisFinanciero; }
}