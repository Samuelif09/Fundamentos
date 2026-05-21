package com.openlib.market.application.busqueda;

import com.openlib.market.domain.busqueda.IBusquedaGateway;
import com.openlib.market.domain.busqueda.LibroBuscado;
import com.openlib.market.domain.busqueda.PalabraClave;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class BuscarBusquedaInteractor implements IBuscarBusquedaUseCase {

    private final IBusquedaGateway busquedaGateway;

    public BuscarBusquedaInteractor(IBusquedaGateway busquedaGateway) {
        this.busquedaGateway = busquedaGateway;
    }

    @Override
    public List<LibroBuscadoDto> buscarPorPalabrasClave(String query) {
        // Validación en el dominio (Value Object)
        PalabraClave palabraClave = new PalabraClave(query);
        
        // Consulta a la puerta de salida (Gateway)
        List<LibroBuscado> librosDominio = busquedaGateway.buscarPorPalabraClave(palabraClave);
        
        // Mapeo a DTO para la capa externa
        return librosDominio.stream()
                .map(libro -> new LibroBuscadoDto(libro.getId(), libro.getTitulo(), libro.getAutor()))
                .collect(Collectors.toList());
    }
}
