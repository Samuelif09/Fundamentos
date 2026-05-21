package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.BannerPromocionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BannerPromocionalRepository extends JpaRepository<BannerPromocionalEntity, String> {

    /**
     * Banners vigentes: estado ACTIVA y la fecha actual está dentro del período.
     */
    @Query("SELECT b FROM BannerPromocionalEntity b WHERE b.estado = 'ACTIVA' " +
           "AND b.fechaInicio <= :ahora AND b.fechaFin >= :ahora")
    List<BannerPromocionalEntity> findVigentes(@Param("ahora") LocalDateTime ahora);
}
