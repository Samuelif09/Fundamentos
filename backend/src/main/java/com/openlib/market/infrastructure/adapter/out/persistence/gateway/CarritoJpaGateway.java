package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.Cantidad;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;
import com.openlib.market.domain.carrito.ItemCarrito;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.CarritoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ItemCarritoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.CarritoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Primary
public class CarritoJpaGateway implements ICarritoGateway {

    private final CarritoRepository repository;

    public CarritoJpaGateway(CarritoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CarritoCompras> obtenerPorSesionId(SesionId sesionId) {
        return repository.findById(sesionId.getValor()).map(this::toDomain);
    }

    @Override
    public Optional<CarritoCompras> obtenerPorUsuario(IdUsuario idUsuario) {
        return repository.findAll().stream()
                .filter(e -> idUsuario.getId().equals(e.getSesionId()))
                .findFirst()
                .map(this::toDomain);
    }

    @Transactional // FIX: Transacción atómica para asegurar flush hacia H2
    @Override
    public void guardar(CarritoCompras carrito) {
        String sesionKey = carrito.getSesionId() != null
                ? carrito.getSesionId().getValor()
                : carrito.getIdUsuario().getId();

        CarritoEntity entity = repository.findById(sesionKey)
                .orElse(new CarritoEntity(sesionKey));

        entity.getItems().clear();
        for (ItemCarrito item : carrito.getItems()) {
            // FIX: Uso del método helper para sincronización bidireccional antes de persistir
            ItemCarritoEntity itemEntity = new ItemCarritoEntity();
            itemEntity.setIsbn(item.getLibroIsbn());
            itemEntity.setCantidad(item.getCantidad().getValor());
            entity.agregarItem(itemEntity);
        }
        repository.save(entity);
    }

    private CarritoCompras toDomain(CarritoEntity entity) {
        CarritoCompras carrito = new CarritoCompras(new SesionId(entity.getSesionId()));
        for (ItemCarritoEntity item : entity.getItems()) {
            carrito.agregarItem(
                    new com.openlib.market.domain.carrito.LibroSnapshot(item.getIsbn(), 0.0),
                    new Cantidad(item.getCantidad())
            );
        }
        return carrito;
    }
}
