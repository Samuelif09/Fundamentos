package com.openlib.market.application.detalle;

import com.openlib.market.domain.detalle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerDetalleInteractorTest {

    private IDetalleGateway gateway;
    private VerDetalleInteractor interactor;

    @BeforeEach
    void setUp() {
        gateway = mock(IDetalleGateway.class);
        interactor = new VerDetalleInteractor(gateway);
    }

    @Test
    void debeRetornarDetalleSiLibroExiste() {
        Isbn isbn = new Isbn("978-3-16-148410-0");
        Libro libro = new Libro(isbn, "Clean Code", "Sinopsis", new Precio(29.99), "url");
        when(gateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libro));

        LibroDetalleDto dto = interactor.verDetalle("978-3-16-148410-0");

        assertNotNull(dto);
        assertEquals("Clean Code", dto.getTitulo());
        assertEquals(29.99, dto.getPrecio());
    }

    @Test
    void debeLanzarExcepcionSiLibroNoExiste() {
        when(gateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class, () -> interactor.verDetalle("978-0-00-000000-0"));
    }
}
