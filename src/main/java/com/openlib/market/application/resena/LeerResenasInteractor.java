package com.openlib.market.application.resena;

import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;

import java.util.List;

public class LeerResenasInteractor implements ILeerResenasUseCase {

    private final IResenaGateway resenaGateway;

    public LeerResenasInteractor(IResenaGateway resenaGateway) {
        this.resenaGateway = resenaGateway;
    }

    @Override
    public List<ResenaResponseDto> leerResenas(String isbnLibro, int offset, int limit) {
        if (isbnLibro == null || isbnLibro.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN del libro es obligatorio");
        }

        List<Resena> resenas = resenaGateway.listarPorLibroId(isbnLibro, offset, limit);

        return resenas.stream()
                .map(r -> new ResenaResponseDto(
                        r.getId(),
                        r.getCalificacion().getValor(),
                        r.getTexto(),
                        r.getFecha()
                ))
                .toList();
    }
}
