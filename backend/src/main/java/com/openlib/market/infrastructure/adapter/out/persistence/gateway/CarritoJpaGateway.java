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
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class CarritoJpaGateway implements ICarritoGateway {

    private final CarritoRepository repository;
    private final ContenidoDigitalRepository bookRepository;

    public CarritoJpaGateway(CarritoRepository repository, ContenidoDigitalRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<CarritoCompras> obtenerPorSesionId(SesionId sesionId) {
        return repository.findById(sesionId.getValor())
                .map(entity -> {
                    CarritoCompras carrito = new CarritoCompras(sesionId);
                    for (ItemCarritoEntity item : entity.getItems()) {
                        double precio = bookRepository.findById(item.getIsbn())
                                .map(com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity::getPrecio)
                                .orElse(0.0);
                        carrito.agregarItem(
                                new com.openlib.market.domain.carrito.LibroSnapshot(item.getIsbn(), precio),
                                new Cantidad(item.getCantidad())
                        );
                    }
                    return carrito;
                });
    }

    @Override
    public Optional<CarritoCompras> obtenerPorUsuario(IdUsuario idUsuario) {
        return repository.findById(idUsuario.getId())
                .map(entity -> {
                    CarritoCompras carrito = new CarritoCompras(idUsuario);
                    for (ItemCarritoEntity item : entity.getItems()) {
                        double precio = bookRepository.findById(item.getIsbn())
                                .map(com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity::getPrecio)
                                .orElse(0.0);
                        carrito.agregarItem(
                                new com.openlib.market.domain.carrito.LibroSnapshot(item.getIsbn(), precio),
                                new Cantidad(item.getCantidad())
                        );
                    }
                    return carrito;
                });
    }

    @Override
    public void guardar(CarritoCompras carrito) {
        String sesionKey = carrito.getSesionId() != null
                ? carrito.getSesionId().getValor()
                : carrito.getIdUsuario().getId();

        CarritoEntity entity = repository.findById(sesionKey)
                .orElse(new CarritoEntity(sesionKey));

        entity.getItems().clear();
        for (ItemCarrito item : carrito.getItems()) {
            entity.addItem(new ItemCarritoEntity(entity, item.getLibroIsbn(), item.getCantidad().getValor()));
        }
        repository.save(entity);
    }
}
