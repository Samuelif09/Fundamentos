package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.CriterioBusqueda;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarCatalogoInteractorTest {

    private ICatalogoGateway gateway;
    private BuscarCatalogoInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(ICatalogoGateway.class);
        interactor = new BuscarCatalogoInteractor(gateway);
    }

    @Test
    void debeRetornarListaDeLibrosDtoSiCriterioValido() {
        when(gateway.buscarPorFiltros(any(CriterioBusqueda.class))).thenReturn(List.of(
                new LibroCatalogo("1", "Clean Code", 40.0, "url")
        ));

        List<LibroCatalogoDto> resultados = interactor.buscar("Clean", null, null, null, null);

        assertEquals(1, resultados.size());
        assertEquals("Clean Code", resultados.get(0).getTitulo());
    }

    @Test
    void debeLanzarExcepcionSiCriteriosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> interactor.buscar(null, " ", null, null, null));
        verify(gateway, never()).buscarPorFiltros(any());
    }

    @Test
    void debeAplicarFiltroDeRangoDePrecio() {
        when(gateway.buscarPorFiltros(any(CriterioBusqueda.class))).thenReturn(List.of(
                new LibroCatalogo("1", "Libro Barato", 10.0, "url")
        ));

        List<LibroCatalogoDto> resultados = interactor.buscar(null, null, null, 5.0, 20.0);

        assertEquals(1, resultados.size());
        assertEquals("Libro Barato", resultados.get(0).getTitulo());
    }
}
