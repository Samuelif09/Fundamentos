package com.openlib.market.application.historial;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.historial.HistorialNavegacion;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VerHistorialNavegacionInteractor implements IVerHistorialNavegacionUseCase {

    private final IHistorialNavegacionGateway historialGateway;
    private final IDetalleGateway detalleGateway;

    public VerHistorialNavegacionInteractor(IHistorialNavegacionGateway historialGateway, IDetalleGateway detalleGateway) {
        this.historialGateway = historialGateway;
        this.detalleGateway = detalleGateway;
    }

    @Override
    public List<ItemHistorialResponseDto> verHistorial(String idUsuario) {
        return historialGateway.obtenerPorUsuario(idUsuario)
                .map(HistorialNavegacion::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(item -> {
                    String titulo = detalleGateway.buscarPorId(new Isbn(item.getIdLibro()))
                            .map(Libro::getTitulo)
                            .orElse("Libro no disponible");
                    return new ItemHistorialResponseDto(item.getIdLibro(), titulo, item.getFechaVista());
                })
                .collect(Collectors.toList());
    }
}
