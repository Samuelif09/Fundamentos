package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoDigitalRepository extends JpaRepository<ContenidoDigitalEntity, String>, JpaSpecificationExecutor<ContenidoDigitalEntity> {
    List<ContenidoDigitalEntity> findByIdVendedor(String idVendedor);
    List<ContenidoDigitalEntity> findByEstado(String estado, org.springframework.data.domain.Pageable pageable);
    
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE ContenidoDigitalEntity c SET c.stockDisponible = c.stockDisponible - 1 WHERE c.isbn = :isbn AND c.stockDisponible > 0")
    int decrementStock(@org.springframework.data.repository.query.Param("isbn") String isbn);
}
