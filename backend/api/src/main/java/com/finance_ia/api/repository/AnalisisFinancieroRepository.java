package com.finance_ia.api.repository;

import com.finance_ia.api.model.AnalisisFinancieroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnalisisFinancieroRepository extends JpaRepository<AnalisisFinancieroEntity, Long> {
    // Hereda métodos CRUD automáticos como save(), saveAll(), findAll(), etc.

    // Metodo que actualiza el análisis guardado.
    Optional<AnalisisFinancieroEntity> findByUsuarioNombre(String usuarioNombre);
}