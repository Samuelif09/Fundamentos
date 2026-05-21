package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.VendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<VendedorEntity, String> {
    boolean existsByIdentificacionTributaria(String identificacionTributaria);
    Optional<VendedorEntity> findByIdUsuario(String idUsuario);
}
