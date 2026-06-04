package com.openlib.market.application.carrito;

public interface IActualizarCarritoUseCase {
    void eliminarItem(String idUsuario, String isbn);
    void actualizarCantidad(String idUsuario, String isbn, int nuevaCantidad);
}
