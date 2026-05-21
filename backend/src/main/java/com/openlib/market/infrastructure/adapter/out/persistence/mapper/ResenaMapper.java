package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.Resena;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ResenaEntity;
import org.springframework.stereotype.Component;

@Component
public class ResenaMapper {

    public Resena toDomain(ResenaEntity entity) {
        return new Resena(
                entity.getId(),
                entity.getIsbnLibro(),
                new Calificacion(entity.getCalificacion()),
                entity.getTexto(),
                entity.getFecha()
        );
    }

    public ResenaEntity toEntity(Resena domain, ContenidoDigitalEntity libro) {
        return new ResenaEntity(
                domain.getId(),
                libro,
                domain.getCalificacion().getValor(),
                domain.getTexto(),
                domain.getFecha()
        );
    }
}
