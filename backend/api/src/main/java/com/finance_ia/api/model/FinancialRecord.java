package com.finance_ia.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_records") // Nombre de la tabla en MySQL
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;       // Fecha de la transacción

    @Column(nullable = false)
    private String description;   // Descripción o concepto

    @Column(nullable = false)
    private BigDecimal amount;    // Monto de la transacción

    @Column(nullable = false)
    private String category;      // Categoría financiera
}
