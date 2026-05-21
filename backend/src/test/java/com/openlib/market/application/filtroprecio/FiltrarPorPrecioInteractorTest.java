package com.openlib.market.application.filtroprecio;

import com.openlib.market.domain.filtroprecio.IFiltroPrecioGateway;
import com.openlib.market.domain.filtroprecio.LibroFiltro;
import com.openlib.market.domain.filtroprecio.RangoPrecio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiltrarPorPrecioInteractorTest {

    private IFiltroPrecioGateway gateway;
    private FiltrarPorPrecioInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IFiltroPrecioGateway.class);
        interactor = new FiltrarPorPrecioInteractor(gateway);
    }

    @Test
    void debeRetornarLibrosEnRango() {
        when(gateway.buscarPorRango(any(RangoPrecio.class))).thenReturn(List.of(
                new LibroFiltro("123", "Libro 1", 20.0),
                new LibroFiltro("124", "Libro 2", 30.0)
        ));

        List<LibroBuscadoDto> resultados = interactor.filtrar(10.0, 40.0);

        assertEquals(2, resultados.size());
        assertEquals(20.0, resultados.get(0).getPrecio());
    }

    @Test
    void debeLanzarExcepcionSiRangoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> interactor.filtrar(50.0, 10.0));
        verify(gateway, never()).buscarPorRango(any());
    }
}
