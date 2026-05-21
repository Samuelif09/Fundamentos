package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.domain.categoria.EstadoCategoria;
import com.openlib.market.domain.categoria.NombreCategoria;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.CategoriaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaCatalogo toDomain(CategoriaEntity entity) {
        return new CategoriaCatalogo(
                entity.getId(),
                new NombreCategoria(entity.getNombre()),
                EstadoCategoria.valueOf(entity.getEstado())
        );
    }

    public CategoriaEntity toEntity(CategoriaCatalogo domain) {
        return new CategoriaEntity(
                domain.getId(),
                domain.getNombre().getValor(),
                domain.getEstado().name()
        );
    }
}
