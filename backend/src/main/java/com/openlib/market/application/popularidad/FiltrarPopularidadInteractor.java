package com.openlib.market.application.popularidad;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.popularidad.IPopularidadGateway;
import com.openlib.market.domain.popularidad.LibroPopularidad;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiltrarPopularidadInteractor implements IFiltrarPopularidadUseCase {

    private final IPopularidadGateway popularidadGateway;

    public FiltrarPopularidadInteractor(IPopularidadGateway popularidadGateway) {
        this.popularidadGateway = popularidadGateway;
    }

    @Override
    public List<LibroPopularDto> filtrarPorPopularidad() {
        return popularidadGateway.obtenerTodos()
                .stream()
                // Ordenar por ventas de mayor a menor
                .sorted(Comparator.comparingInt(LibroPopularidad::getVentasUltimoMes).reversed())
                .map(l -> new LibroPopularDto(l.getIsbn(), l.getTitulo(), l.getVentasUltimoMes()))
                .collect(Collectors.toList());
    }
}
