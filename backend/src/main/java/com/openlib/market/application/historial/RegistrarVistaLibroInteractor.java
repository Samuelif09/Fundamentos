package com.openlib.market.application.historial;

import com.openlib.market.domain.historial.HistorialNavegacion;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.historial.LibroVistoEvent;

import java.util.ArrayList;

public class RegistrarVistaLibroInteractor {

    private final IHistorialNavegacionGateway historialGateway;

    public RegistrarVistaLibroInteractor(IHistorialNavegacionGateway historialGateway) {
        this.historialGateway = historialGateway;
    }

    public void manejarLibroVisto(LibroVistoEvent evento) {
        HistorialNavegacion historial = historialGateway.obtenerPorUsuario(evento.getIdUsuario())
                .orElseGet(() -> new HistorialNavegacion(evento.getIdUsuario(), new ArrayList<>()));

        historial.registrarVista(evento.getIdLibro(), evento.getFechaVista());

        historialGateway.guardar(historial);
    }
}
