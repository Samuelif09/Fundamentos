package com.openlib.market.application.carrito;

public interface IActualizarItemCarritoUseCase {
    void actualizarCantidad(String userId, String isbn, int cantidad);
    void eliminarItem(String userId, String isbn);
}
