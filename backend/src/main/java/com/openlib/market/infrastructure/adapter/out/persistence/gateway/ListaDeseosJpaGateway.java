package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.listadeseos.IListaDeseosGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ListaDeseosEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ListaDeseosRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Primary
public class ListaDeseosJpaGateway implements IListaDeseosGateway {

    private final ListaDeseosRepository repository;

    public ListaDeseosJpaGateway(ListaDeseosRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> obtenerUsuariosInteresados(String idLibro) {
        return repository.findUsuariosInteresadosPorIsbn(idLibro);
    }

    @Override
    public void guardar(String idUsuario, Set<String> isbns) {
        ListaDeseosEntity entity = repository.findById(idUsuario).orElse(new ListaDeseosEntity(idUsuario, isbns));
        entity.setIsbns(isbns);
        repository.save(entity);
    }

    @Override
    public Set<String> obtenerPorUsuario(String idUsuario) {
        return repository.findById(idUsuario).map(ListaDeseosEntity::getIsbns).orElse(Set.of());
    }
}
