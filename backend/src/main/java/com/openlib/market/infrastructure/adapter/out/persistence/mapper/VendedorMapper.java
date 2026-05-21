package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.vendedor.EstadoVerificacion;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.VendedorEntity;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public VendedorEntity toEntity(Vendedor vendedor) {
        VendedorEntity entity = new VendedorEntity();
        entity.setId(vendedor.getId());
        entity.setIdUsuario(vendedor.getIdUsuario());
        entity.setRazonSocial(vendedor.getRazonSocial().getValor());
        entity.setIdentificacionTributaria(vendedor.getIdentificacionTributaria().getValor());
        entity.setEstadoVerificacion(vendedor.getEstadoVerificacion().name());
        return entity;
    }

    public Vendedor toDomain(VendedorEntity entity) {
        return new Vendedor(
                entity.getId(),
                entity.getIdUsuario(),
                new RazonSocial(entity.getRazonSocial()),
                new IdentificacionTributaria(entity.getIdentificacionTributaria()),
                EstadoVerificacion.valueOf(entity.getEstadoVerificacion())
        );
    }
}
