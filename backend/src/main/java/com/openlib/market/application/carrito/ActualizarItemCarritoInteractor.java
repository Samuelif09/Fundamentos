package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.*;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;

public class ActualizarItemCarritoInteractor implements IActualizarItemCarritoUseCase {

    private final ICarritoGateway carritoGateway;
    private final IInventarioGateway inventarioGateway;

    public ActualizarItemCarritoInteractor(ICarritoGateway carritoGateway, IInventarioGateway inventarioGateway) {
        this.carritoGateway = carritoGateway;
        this.inventarioGateway = inventarioGateway;
    }

    @Override
    public void actualizarCantidad(String userId, String isbn, int cantidad) {
        if (cantidad <= 0) {
            eliminarItem(userId, isbn);
            return;
        }

        // Validar Stock
        StockDisponible stock = inventarioGateway.obtenerStock(isbn)
                .orElse(new StockDisponible(0));

        if (stock.getCantidad() < cantidad) {
            throw new StockInsuficienteException(isbn, cantidad, stock.getCantidad());
        }

        IdUsuario idUsuario = new IdUsuario(userId);
        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.actualizarCantidad(isbn, new Cantidad(cantidad));
        carritoGateway.guardar(carrito);
    }

    @Override
    public void eliminarItem(String userId, String isbn) {
        IdUsuario idUsuario = new IdUsuario(userId);
        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.removerItem(isbn);
        carritoGateway.guardar(carrito);
    }
}
