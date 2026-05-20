package com.openlib.market.application.popularidad;

import com.openlib.market.domain.popularidad.IPopularidadGateway;
import com.openlib.market.domain.popularidad.LibroPopularidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiltrarPopularidadInteractorTest {

    private IPopularidadGateway gateway;
    private FiltrarPopularidadInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IPopularidadGateway.class);
        interactor = new FiltrarPopularidadInteractor(gateway);
    }

    @Test
    void debeRetornarLibrosOrdenadosPorVentasDescendente() {
        when(gateway.obtenerTodos()).thenReturn(List.of(
                new LibroPopularidad("1", "A", 10),
                new LibroPopularidad("2", "B", 50),
                new LibroPopularidad("3", "C", 5)
        ));

        List<LibroPopularDto> resultados = interactor.filtrarPorPopularidad();

        assertEquals(3, resultados.size());
        assertEquals("B", resultados.get(0).getTitulo());
        assertEquals(50, resultados.get(0).getVentasUltimoMes());
        assertEquals("A", resultados.get(1).getTitulo());
        assertEquals("C", resultados.get(2).getTitulo());
    }
}
