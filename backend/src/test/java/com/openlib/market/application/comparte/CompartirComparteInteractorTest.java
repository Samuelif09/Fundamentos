package com.openlib.market.application.comparte;

import com.openlib.market.domain.comparte.ILibroComparteGateway;
import com.openlib.market.domain.comparte.LibroNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompartirComparteInteractorTest {

    private ILibroComparteGateway libroGateway;
    private CompartirComparteInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroComparteGateway.class);
        interactor = new CompartirComparteInteractor(libroGateway);
    }

    @Test
    void debeGenerarEnlaceSiLibroExiste() {
        when(libroGateway.existeLibroActivo("978-3-16-148410-0")).thenReturn(true);

        EnlaceDto dto = interactor.generarEnlace("978-3-16-148410-0");

        assertNotNull(dto);
        assertEquals("https://openlib.market/libros/978-3-16-148410-0?utm_source=share", dto.getUrl());
        verify(libroGateway, times(1)).existeLibroActivo("978-3-16-148410-0");
    }

    @Test
    void debeLanzarExcepcionSiLibroNoExiste() {
        when(libroGateway.existeLibroActivo("isbn-inv")).thenReturn(false);

        assertThrows(LibroNoDisponibleException.class, () -> interactor.generarEnlace("isbn-inv"));
        verify(libroGateway, times(1)).existeLibroActivo("isbn-inv");
    }
}
