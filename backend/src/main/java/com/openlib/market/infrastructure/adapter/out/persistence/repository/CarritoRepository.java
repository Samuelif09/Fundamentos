package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoEntity, String> {

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query("DELETE FROM CarritoEntity c WHERE c.sesionId = :sesionId")
    void borrarDirectoPorSesionId(@org.springframework.data.repository.query.Param("sesionId") String sesionId);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query("DELETE FROM ItemCarritoEntity i WHERE i.carrito.sesionId = :sesionId")
    void borrarItemsPorSesionId(@org.springframework.data.repository.query.Param("sesionId") String sesionId);
}
