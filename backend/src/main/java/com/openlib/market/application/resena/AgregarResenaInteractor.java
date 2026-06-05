package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;

import java.time.LocalDate;
import java.util.UUID;

public class AgregarResenaInteractor implements IAgregarResenaUseCase {

    private final IResenaGateway resenaGateway;

    public AgregarResenaInteractor(IResenaGateway resenaGateway) {
        this.resenaGateway = resenaGateway;
    }

    @Override
    public void ejecutar(String isbn, AgregarResenaRequestDto request) {
        String id = UUID.randomUUID().toString();
        Calificacion calificacion = new Calificacion(request.getCalificacion());
        Resena resena = new Resena(id, isbn, calificacion, request.getTexto(), LocalDate.now());
        
        resenaGateway.actualizar(resena);
    }
}
