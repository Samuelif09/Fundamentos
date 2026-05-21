package com.openlib.market.application.resena;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VerResenasInteractor implements IVerResenasUseCase {

    private final IResenaGateway resenaGateway;

    public VerResenasInteractor(IResenaGateway resenaGateway) {
        this.resenaGateway = resenaGateway;
    }

    @Override
    public List<ResenaDto> verResenas(String isbnLibro) {
        List<Resena> resenas = resenaGateway.buscarResenasPorIsbn(isbnLibro);
        
        return resenas.stream()
                .map(r -> new ResenaDto(
                        r.getId(),
                        r.getCalificacion().getValor(),
                        r.getTexto(),
                        r.getFecha()
                ))
                .collect(Collectors.toList());
    }
}
