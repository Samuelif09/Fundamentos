package com.openlib.market.application.inventario;

import com.openlib.market.domain.detalle.AccesoDenegadoLibroException;
import com.openlib.market.domain.detalle.IActualizarLibroGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ActualizarInventarioInteractorTest {

    private IActualizarLibroGateway libroGateway;
    private ActualizarInventarioInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(IActualizarLibroGateway.class);
        interactor = new ActualizarInventarioInteractor(libroGateway);
    }

    @Test
    void debeActualizarPrecioSiVendedorEsElPropietario() {
        Libro libroOriginal = new Libro(
                new Isbn("ISBN-001"), "Libro X", "Sinopsis X",
                new Precio(10.0), "url", "Cat", "seller-1"
        );
        when(libroGateway.buscarPorIsbn(new Isbn("ISBN-001"))).thenReturn(Optional.of(libroOriginal));

        ActualizarPrecioRequestDto request = new ActualizarPrecioRequestDto("seller-1", "ISBN-001", 15.0);
        interactor.actualizarPrecio(request);

        verify(libroGateway, times(1)).actualizar(argThat(l -> l.getPrecio().getValor() == 15.0));
    }

    @Test
    void debeLanzarAccesoDenegadoSiVendedorNoEsPropietario() {
        Libro libroDe_seller1 = new Libro(
                new Isbn("ISBN-001"), "Libro X", "Sinopsis X",
                new Precio(10.0), "url", "Cat", "seller-1"
        );
        when(libroGateway.buscarPorIsbn(new Isbn("ISBN-001"))).thenReturn(Optional.of(libroDe_seller1));

        ActualizarPrecioRequestDto request = new ActualizarPrecioRequestDto("seller-2", "ISBN-001", 5.0);

        assertThrows(AccesoDenegadoLibroException.class, () -> interactor.actualizarPrecio(request));
        verify(libroGateway, never()).actualizar(any());
    }

    @Test
    void debeLanzarExcepcionSiPrecioEsNegativo() {
        Libro libro = new Libro(
                new Isbn("ISBN-001"), "Libro X", "Sinopsis X",
                new Precio(10.0), "url", "Cat", "seller-1"
        );
        when(libroGateway.buscarPorIsbn(new Isbn("ISBN-001"))).thenReturn(Optional.of(libro));

        ActualizarPrecioRequestDto request = new ActualizarPrecioRequestDto("seller-1", "ISBN-001", -3.0);

        // Precio negativo falla en el Value Object Precio
        assertThrows(IllegalArgumentException.class, () -> interactor.actualizarPrecio(request));
        verify(libroGateway, never()).actualizar(any());
    }

    @Test
    void debeLanzarExcepcionSiLibroNoExiste() {
        when(libroGateway.buscarPorIsbn(any())).thenReturn(Optional.empty());

        ActualizarPrecioRequestDto request = new ActualizarPrecioRequestDto("seller-1", "ISBN-XXX", 5.0);

        assertThrows(IllegalArgumentException.class, () -> interactor.actualizarPrecio(request));
        verify(libroGateway, never()).actualizar(any());
    }
}
