package com.finance_ia.api.repository;

import com.finance_ia.api.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Método de Spring Data para buscar un usuario por su correo electrónico
    Optional<UsuarioEntity> findByEmail(String email);
}