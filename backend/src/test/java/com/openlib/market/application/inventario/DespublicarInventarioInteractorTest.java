package com.openlib.market.application.inventario;

import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DespublicarInventarioInteractorTest {

    private ILibroPublicacionGateway libroGateway;
    private DespublicarInventarioInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroPublicacionGateway.class);
        interactor = new DespublicarInventarioInteractor(libroGateway);
    }

    @Test
    void debeDespublicarLibroExitosamenteSiEsPropietario() {
        Libro libroActivo = new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1", EstadoLibro.PUBLICADO, null);
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(libroActivo));

        interactor.despublicar("seller-1", "isbn-1");

        verify(libroGateway).actualizar(argThat(l -> l.getEstado() == EstadoLibro.PAUSADO));
    }

    @Test
    void debeLanzarExcepcionSiVendedorNoEsPropietario() {
        Libro libroActivo = new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1", EstadoLibro.PUBLICADO, null);
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(libroActivo));

        assertThrows(IllegalStateException.class, () -> interactor.despublicar("hacker-2", "isbn-1"));
        verify(libroGateway, never()).actualizar(any());
    }

    @Test
    void debeLanzarExcepcionSiLibroNoExiste() {
        when(libroGateway.obtenerPorIsbn("isbn-X")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> interactor.despublicar("seller-1", "isbn-X"));
    }
}
