package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import com.openlib.market.domain.wishlist.ListaDeseos;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ListaDeseosEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ListaDeseosRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WishlistJpaGateway implements IListaDeseosGateway {

    private final ListaDeseosRepository repository;

    public WishlistJpaGateway(ListaDeseosRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ListaDeseos> obtenerPorUsuario(String idUsuario) {
        return repository.findById(idUsuario)
                .map(entity -> new ListaDeseos(entity.getIdUsuario(), entity.getIsbns()));
    }

    @Override
    public void guardar(ListaDeseos lista) {
        ListaDeseosEntity entity = repository.findById(lista.getIdUsuario())
                .orElse(new ListaDeseosEntity(lista.getIdUsuario()));
        
        // FIX DEFINITIVO: Mantener la referencia original de Hibernate
        entity.getIsbns().clear();
        entity.getIsbns().addAll(lista.getIsbns());
        
        repository.save(entity);
    }
}
