package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.EstadoResena;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;

public class ModerarResenaInteractor implements IModerarResenaUseCase {

    private final IResenaGateway resenaGateway;

    public ModerarResenaInteractor(IResenaGateway resenaGateway) {
        this.resenaGateway = resenaGateway;
    }

    @Override
    public void ejecutar(String idResena, ModerarResenaRequestDto request) {
        Resena resena = resenaGateway.obtenerPorId(idResena)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada con ID: " + idResena));

        EstadoResena nuevoEstado;
        try {
            nuevoEstado = EstadoResena.valueOf(request.getEstado());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Estado inválido: " + request.getEstado());
        }

        resena.moderar(nuevoEstado, request.getMotivo());
        resenaGateway.actualizar(resena);
    }
}
