package com.openlib.market.domain.inventario;

import java.util.Optional;

public interface IInventarioGateway {
    Optional<StockDisponible> obtenerStock(String isbn);
    void agregarStock(String productoId, int cantidad);
    void restarStock(String productoId, int cantidad);
}
