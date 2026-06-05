package com.openlib.market.infrastructure.carrito;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.SesionId;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CarritoJsonGateway implements ICarritoGateway {

    // Simula almacenamiento en memoria/Redis
    private final Map<String, CarritoCompras> carritosMemoria = new HashMap<>();

    @Override
    public Optional<CarritoCompras> obtenerPorSesionId(SesionId sesionId) {
        return Optional.ofNullable(carritosMemoria.get(sesionId.getValor()));
    }

    @Override
    public Optional<CarritoCompras> obtenerPorUsuario(com.openlib.market.domain.carrito.IdUsuario idUsuario) {
        return Optional.ofNullable(carritosMemoria.get(idUsuario.getId()));
    }

    @Override
    public void guardar(CarritoCompras carrito) {
        if (carrito.getIdUsuario() != null) {
            carritosMemoria.put(carrito.getIdUsuario().getId(), carrito);
        } else if (carrito.getSesionId() != null) {
            carritosMemoria.put(carrito.getSesionId().getValor(), carrito);
        }
    }

    @Override
    public void eliminarPorSesionId(SesionId sesionId) {
        carritosMemoria.remove(sesionId.getValor());
    }
}
