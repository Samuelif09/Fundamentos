package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionFinancieraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaccionFinancieraRepository extends JpaRepository<TransaccionFinancieraEntity, String> {

    List<TransaccionFinancieraEntity> findByIdVendedor(String idVendedor);

    @Query("SELECT t FROM TransaccionFinancieraEntity t WHERE t.idVendedor = :idVendedor AND t.fecha BETWEEN :desde AND :hasta")
    List<TransaccionFinancieraEntity> findByVendedorYPeriodo(
            @Param("idVendedor") String idVendedor,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );

    @Query("SELECT COALESCE(SUM(t.subtotal), 0.0) FROM TransaccionFinancieraEntity t WHERE t.idVendedor = :idVendedor AND t.fecha BETWEEN :desde AND :hasta")
    Double sumarIngresosPorPeriodo(
            @Param("idVendedor") String idVendedor,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta
    );
}
