package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.ResenaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaRepository extends JpaRepository<ResenaEntity, String> {

    @Query("SELECT AVG(r.calificacion) FROM ResenaEntity r WHERE r.isbnLibro = :isbn")
    Double calcularPromedioPorIsbn(@Param("isbn") String isbn);
}
