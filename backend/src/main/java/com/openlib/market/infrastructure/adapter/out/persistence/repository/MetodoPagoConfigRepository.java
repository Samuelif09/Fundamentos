package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.MetodoPagoConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoConfigRepository extends JpaRepository<MetodoPagoConfigEntity, String> {
    long countByEstado(String estado);
}
