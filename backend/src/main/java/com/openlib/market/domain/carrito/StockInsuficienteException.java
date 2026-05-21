package com.openlib.market.domain.carrito;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String isbn, int solicitado, int disponible) {
        super(String.format("Stock insuficiente para el libro %s. Solicitado: %d, Disponible: %d", isbn, solicitado, disponible));
    }
}
