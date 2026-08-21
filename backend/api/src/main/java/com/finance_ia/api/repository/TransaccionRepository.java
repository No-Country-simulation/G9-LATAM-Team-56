package com.finance_ia.api.repository;

import com.finance_ia.api.model.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionEntity, Long> {

    // Navega de Transaccion -> AnalisisFinanciero -> usuarioNombre, filtra, ordena y limita a 10
    List<TransaccionEntity> findTop10ByAnalisisFinanciero_UsuarioNombreOrderByFechaDesc(String usuarioNombre);

    // Busca transacciones de un usuario dentro de un rango de fechas
    List<TransaccionEntity> findByAnalisisFinanciero_UsuarioNombreAndFechaBetween(
            String usuarioNombre,
            LocalDate inicio,
            LocalDate fin
    );
}