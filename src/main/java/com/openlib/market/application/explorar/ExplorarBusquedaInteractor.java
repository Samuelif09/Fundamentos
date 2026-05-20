package com.openlib.market.application.explorar;

import com.openlib.market.domain.explorar.CriterioTendencia;
import com.openlib.market.domain.explorar.ITendenciaGateway;
import com.openlib.market.domain.explorar.LibroTendencia;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExplorarBusquedaInteractor implements IExplorarBusquedaUseCase {

    private final ITendenciaGateway tendenciaGateway;

    public ExplorarBusquedaInteractor(ITendenciaGateway tendenciaGateway) {
        this.tendenciaGateway = tendenciaGateway;
    }

    @Override
    public List<LibroTendenciaDto> explorarTendencias(CriterioTendencia criterio) {
        List<LibroTendencia> todosLosLibros = tendenciaGateway.obtenerTodos();

        Comparator<LibroTendencia> comparador = switch (criterio) {
            case MAS_VENDIDOS -> Comparator.comparingInt(LibroTendencia::getVentasTotales).reversed();
            case MEJOR_CALIFICADOS -> Comparator.comparingDouble(LibroTendencia::getCalificacion).reversed();
            case NUEVOS -> Comparator.comparing(LibroTendencia::getFechaPublicacion).reversed();
        };

        // Regla de Negocio: Top 10 máximo
        return todosLosLibros.stream()
                .sorted(comparador)
                .limit(10)
                .map(libro -> new LibroTendenciaDto(libro.getIsbn(), libro.getTitulo()))
                .collect(Collectors.toList());
    }
}
