package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.Resena;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ResenaEntity;
import org.springframework.stereotype.Component;

@Component
public class ResenaMapper {

    public Resena toDomain(ResenaEntity entity) {
        Resena resena = new Resena(
                entity.getId(),
                entity.getIsbnLibro(),
                new Calificacion(entity.getCalificacion()),
                entity.getTexto(),
                entity.getFecha()
        );
        if (entity.getEstado() != null) {
            resena.restaurarEstado(com.openlib.market.domain.resena.EstadoResena.valueOf(entity.getEstado()), entity.getMotivo());
        }
        return resena;
    }

    public ResenaEntity toEntity(Resena domain, ContenidoDigitalEntity libro) {
        return new ResenaEntity(
                domain.getId(),
                libro,
                domain.getCalificacion().getValor(),
                domain.getTexto(),
                domain.getFecha(),
                domain.getEstado().name(),
                domain.getMotivoModeracion()
        );
    }
}
