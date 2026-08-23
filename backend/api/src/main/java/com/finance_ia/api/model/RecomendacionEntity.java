package com.finance_ia.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recomendacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 500)
  private String mensaje;

  @Column(nullable = false)
  private String categoria;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "analisis_financiero_id",
      nullable = false
  )
  private AnalisisFinancieroEntity analisisFinanciero;
}
