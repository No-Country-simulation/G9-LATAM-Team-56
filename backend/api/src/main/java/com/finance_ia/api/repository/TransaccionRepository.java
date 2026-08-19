package com.finance_ia.api.repository;

import com.finance_ia.api.model.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionEntity, Long> {

    // Navega de Transaccion -> AnalisisFinanciero -> usuarioNombre, filtra, ordena y limita a 10
    List<TransaccionEntity> findTop10ByAnalisisFinanciero_UsuarioNombreOrderByFechaDesc(String usuarioNombre);
}