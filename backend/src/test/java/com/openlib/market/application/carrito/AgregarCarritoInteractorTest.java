package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.*;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Audiolibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgregarCarritoInteractorTest {

    private ICarritoGateway carritoGateway;
    private ILibroGateway libroGateway;
    private IInventarioGateway inventarioGateway;
    private IContenidoDigitalGateway contenidoGateway;
    private AgregarCarritoInteractor interactor;

    @BeforeEach
    void setUp() {
        carritoGateway = mock(ICarritoGateway.class);
        libroGateway = mock(ILibroGateway.class);
        inventarioGateway = mock(IInventarioGateway.class);
        contenidoGateway = mock(IContenidoDigitalGateway.class);
        interactor = new AgregarCarritoInteractor(carritoGateway, libroGateway, inventarioGateway, contenidoGateway);
    }

    @Test
    void debeLanzarExcepcionSiStockEsInsuficiente() {
        ContenidoDigital libroMock = mock(Libro.class);
        when(libroMock.getEstado()).thenReturn(EstadoLibro.PUBLICADO);
        when(libroMock.requiereControlDeInventario()).thenReturn(true);
        when(contenidoGateway.obtenerContenidoPorId("123")).thenReturn(Optional.of(libroMock));
        
        when(inventarioGateway.obtenerStock("123")).thenReturn(Optional.of(new StockDisponible(1)));

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-1", "123", 2);

        assertThrows(StockInsuficienteException.class, () -> interactor.agregarAlCarrito(req));
        verify(carritoGateway, never()).guardar(any());
    }

    @Test
    void debeAgregarItemAlCarritoDeUsuarioExitosamente() {
        ContenidoDigital libroMock = mock(Libro.class);
        when(libroMock.getEstado()).thenReturn(EstadoLibro.PUBLICADO);
        when(libroMock.requiereControlDeInventario()).thenReturn(true);
        when(contenidoGateway.obtenerContenidoPorId("123")).thenReturn(Optional.of(libroMock));

        when(inventarioGateway.obtenerStock("123")).thenReturn(Optional.of(new StockDisponible(5)));
        when(libroGateway.obtenerPorIsbn("123")).thenReturn(Optional.of(new LibroSnapshot("123", 20.0)));
        when(carritoGateway.obtenerPorUsuario(new IdUsuario("user-1"))).thenReturn(Optional.empty());

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-1", "123", 2);

        assertDoesNotThrow(() -> interactor.agregarAlCarrito(req));

        verify(carritoGateway, times(1)).guardar(any(CarritoCompras.class));
    }

    @Test
    void debeAgregarAudiolibroSinLlamarInventarioGateway() {
        ContenidoDigital audiolibroMock = mock(Audiolibro.class);
        when(audiolibroMock.getEstado()).thenReturn(EstadoLibro.PUBLICADO);
        when(audiolibroMock.requiereControlDeInventario()).thenReturn(false);
        when(contenidoGateway.obtenerContenidoPorId("456")).thenReturn(Optional.of(audiolibroMock));

        when(libroGateway.obtenerPorIsbn("456")).thenReturn(Optional.of(new LibroSnapshot("456", 15.0)));
        when(carritoGateway.obtenerPorUsuario(new IdUsuario("user-2"))).thenReturn(Optional.empty());

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-2", "456", 1);

        assertDoesNotThrow(() -> interactor.agregarAlCarrito(req));

        verify(inventarioGateway, never()).obtenerStock(anyString());
        verify(carritoGateway, times(1)).guardar(any(CarritoCompras.class));
    }

    @Test
    void debeLanzarExcepcionSiEsLibroFisicoYStockCero() {
        ContenidoDigital libroMock = mock(Libro.class);
        when(libroMock.getEstado()).thenReturn(EstadoLibro.PUBLICADO);
        when(libroMock.requiereControlDeInventario()).thenReturn(true);
        when(contenidoGateway.obtenerContenidoPorId("789")).thenReturn(Optional.of(libroMock));

        when(inventarioGateway.obtenerStock("789")).thenReturn(Optional.of(new StockDisponible(0)));

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-3", "789", 1);

        assertThrows(StockInsuficienteException.class, () -> interactor.agregarAlCarrito(req));
        verify(carritoGateway, never()).guardar(any());
    }
}
