package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public Pedido toDomain(PedidoEntity entity) {
        java.util.List<com.openlib.market.domain.pago.ItemPedido> items = java.util.Collections.emptyList();
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(i -> new com.openlib.market.domain.pago.ItemPedido(i.getIsbn(), i.getCantidad(), i.getPrecioUnitario()))
                    .collect(java.util.stream.Collectors.toList());
        }

        return new Pedido(
                entity.getId(),
                entity.getSesionId(),
                entity.getIdUsuario(),
                entity.getTotal(),
                EstadoPedido.valueOf(entity.getEstado()),
                entity.getFecha(),
                entity.getTipoMetodoPago() != null ? TipoMetodoPago.valueOf(entity.getTipoMetodoPago()) : null,
                items
        );
    }

    public PedidoEntity toEntity(Pedido domain) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(domain.getId());
        entity.setSesionId(domain.getSesionId());
        entity.setIdUsuario(domain.getIdUsuario());
        entity.setTotal(domain.getTotal());
        entity.setEstado(domain.getEstado().name());
        entity.setFecha(domain.getFecha());
        entity.setTipoMetodoPago(domain.getTipoMetodoPago() != null ? domain.getTipoMetodoPago().name() : null);
        
        if (domain.getItems() != null) {
            for (com.openlib.market.domain.pago.ItemPedido i : domain.getItems()) {
                com.openlib.market.infrastructure.adapter.out.persistence.entity.ItemPedidoEntity itemEntity = new com.openlib.market.infrastructure.adapter.out.persistence.entity.ItemPedidoEntity(
                        entity, i.getIsbn(), i.getCantidad(), i.getPrecioUnitario()
                );
                entity.addItem(itemEntity);
            }
        }
        return entity;
    }
}
