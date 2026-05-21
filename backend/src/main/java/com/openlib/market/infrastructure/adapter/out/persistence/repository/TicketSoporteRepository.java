package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.TicketSoporteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSoporteRepository extends JpaRepository<TicketSoporteEntity, String> {

    /**
     * Tickets filtrados por lista de estados, ordenados por prioridad (ALTA primero) y fecha.
     * Usa CASE para mapear la prioridad enum a un orden numérico.
     */
    @Query("SELECT t FROM TicketSoporteEntity t " +
           "WHERE t.estado IN :estados " +
           "ORDER BY CASE t.prioridad WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 WHEN 'BAJA' THEN 3 END ASC, " +
           "t.fechaCreacion ASC")
    List<TicketSoporteEntity> findByEstadoInOrdenado(@Param("estados") List<String> estados, Pageable pageable);
}
