package com.openlib.market.application.inventario;

public interface IAbastecerInventarioUseCase {
    void ejecutar(String productoId, int cantidad);
}
