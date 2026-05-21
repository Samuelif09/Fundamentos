package com.openlib.market.application.filtroprecio;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.filtroprecio.IFiltroPrecioGateway;
import com.openlib.market.domain.filtroprecio.RangoPrecio;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiltrarPorPrecioInteractor implements IFiltrarPorPrecioUseCase {

    private final IFiltroPrecioGateway filtroPrecioGateway;

    public FiltrarPorPrecioInteractor(IFiltroPrecioGateway filtroPrecioGateway) {
        this.filtroPrecioGateway = filtroPrecioGateway;
    }

    @Override
    public List<LibroBuscadoDto> filtrar(double min, double max) {
        RangoPrecio rango = new RangoPrecio(min, max);
        
        return filtroPrecioGateway.buscarPorRango(rango)
                .stream()
                .map(l -> new LibroBuscadoDto(l.getIsbn(), l.getTitulo(), l.getPrecio()))
                .collect(Collectors.toList());
    }
}
