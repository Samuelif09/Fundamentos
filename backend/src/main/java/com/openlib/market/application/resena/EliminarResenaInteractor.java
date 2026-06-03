package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.IResenaGateway;

public class EliminarResenaInteractor implements IEliminarResenaUseCase {

    private final IResenaGateway resenaGateway;

    public EliminarResenaInteractor(IResenaGateway resenaGateway) {
        this.resenaGateway = resenaGateway;
    }

    @Override
    public void ejecutar(String idResena) {
        resenaGateway.eliminar(idResena);
    }
}
