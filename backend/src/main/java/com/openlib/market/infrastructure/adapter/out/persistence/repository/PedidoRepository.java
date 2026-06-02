package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, String> {
    List<PedidoEntity> findByIdUsuario(String idUsuario, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT i.isbn FROM PedidoEntity p JOIN p.items i WHERE p.idUsuario = :idUsuario AND p.estado = 'PAGADO'")
    List<String> findLibrosCompradosPorUsuario(@org.springframework.data.repository.query.Param("idUsuario") String idUsuario);

    @org.springframework.data.jpa.repository.Query("SELECT p.fecha AS fecha, i.cantidad AS cantidad, i.precioUnitario AS precioUnitario, p.id AS pedidoId " +
           "FROM PedidoEntity p JOIN p.items i, ContenidoDigitalEntity c " +
           "WHERE i.isbn = c.isbn AND c.idVendedor = :idVendedor AND p.estado = 'PAGADO'")
    List<VentaVendedorProjection> findVentasPorVendedor(@org.springframework.data.repository.query.Param("idVendedor") String idVendedor);
}
