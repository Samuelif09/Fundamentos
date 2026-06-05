package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;

public class ActualizarCarritoInteractor implements IActualizarCarritoUseCase {
    private final ICarritoGateway carritoGateway;

    public ActualizarCarritoInteractor(ICarritoGateway carritoGateway) {
        this.carritoGateway = carritoGateway;
    }

    @Override
    public void eliminarItem(String idUsuario, String isbn) {
        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(new IdUsuario(idUsuario))
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        carrito.removerItem(isbn);
        carritoGateway.guardar(carrito);
    }

    @Override
    public void actualizarCantidad(String idUsuario, String isbn, int nuevaCantidad) {
        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(new IdUsuario(idUsuario))
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        carrito.actualizarCantidad(isbn, nuevaCantidad);
        carritoGateway.guardar(carrito);
    }
}
