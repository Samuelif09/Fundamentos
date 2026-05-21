package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.ListaDeseosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaDeseosRepository extends JpaRepository<ListaDeseosEntity, String> {

    @Query(value = "SELECT id_usuario FROM items_lista_deseos WHERE isbn = :isbn", nativeQuery = true)
    List<String> findUsuariosInteresadosPorIsbn(@Param("isbn") String isbn);
}
