package com.openlib.market.application.inventario;

import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.shared.ReglaNegocioInvalidaException;

public class AbastecerInventarioInteractor implements IAbastecerInventarioUseCase {

    private final IInventarioGateway inventarioGateway;

    public AbastecerInventarioInteractor(IInventarioGateway inventarioGateway) {
        this.inventarioGateway = inventarioGateway;
    }

    @Override
    public void ejecutar(String productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new ReglaNegocioInvalidaException("La cantidad a abastecer debe ser mayor a 0");
        }

        inventarioGateway.agregarStock(productoId, cantidad);
    }
}
