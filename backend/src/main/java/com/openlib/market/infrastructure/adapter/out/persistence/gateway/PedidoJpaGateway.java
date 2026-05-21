package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.PedidoMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class PedidoJpaGateway implements IPedidoGateway {

    private final PedidoRepository repository;
    private final PedidoMapper mapper;

    public PedidoJpaGateway(PedidoRepository repository, PedidoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Pedido pedido) {
        repository.save(mapper.toEntity(pedido));
        repository.flush();
    }

    @Override
    public List<Pedido> listarPorUsuarioId(String idUsuario, int offset, int limit) {
        int page = offset / limit;
        return repository.findByIdUsuario(idUsuario, PageRequest.of(page, limit)).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> listarTodos(int page, int size) {
        return repository.findAll(PageRequest.of(page, size)).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obtenerPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
