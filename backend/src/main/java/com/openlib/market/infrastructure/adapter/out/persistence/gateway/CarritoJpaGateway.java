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
import com.openlib.market.domain.carrito.ILibroGateway;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Primary
public class CarritoJpaGateway implements ICarritoGateway {

    private final CarritoRepository repository;
    private final ILibroGateway libroGateway;

    public CarritoJpaGateway(CarritoRepository repository, ILibroGateway libroGateway) {
        this.repository = repository;
        this.libroGateway = libroGateway;
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

    @Transactional
    @Override
    public void eliminarPorSesionId(SesionId sesionId) {
        repository.borrarItemsPorSesionId(sesionId.getValor());
        repository.borrarDirectoPorSesionId(sesionId.getValor());
    }

    private CarritoCompras toDomain(CarritoEntity entity) {
        CarritoCompras carrito = new CarritoCompras(new SesionId(entity.getSesionId()));
        for (ItemCarritoEntity item : entity.getItems()) {
            double precio = libroGateway.obtenerPorIsbn(item.getIsbn())
                    .map(com.openlib.market.domain.carrito.LibroSnapshot::getPrecio)
                    .orElse(0.0);
            carrito.agregarItem(
                    new com.openlib.market.domain.carrito.LibroSnapshot(item.getIsbn(), precio),
                    new Cantidad(item.getCantidad())
            );
        }
        return carrito;
    }
}
