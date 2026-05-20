package com.openlib.market.application.detalle;

import com.openlib.market.domain.detalle.*;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerDetalleLibroInteractorTest {

    private IDetalleGateway detalleGateway;
    private IInventarioGateway inventarioGateway;
    private com.openlib.market.domain.shared.IEventPublisher eventPublisher;
    private VerDetalleLibroInteractor interactor;

    @BeforeEach
    void setUp() {
        detalleGateway = mock(IDetalleGateway.class);
        inventarioGateway = mock(com.openlib.market.domain.inventario.IInventarioGateway.class);
        eventPublisher = mock(com.openlib.market.domain.shared.IEventPublisher.class);
        com.openlib.market.domain.inventario.IPromocionGateway promocionGateway = mock(com.openlib.market.domain.inventario.IPromocionGateway.class);
        interactor = new VerDetalleLibroInteractor(detalleGateway, inventarioGateway, eventPublisher, promocionGateway);
    }

    @Test
    void debeRetornarLibroDisponibleSiHayStock() {
        Libro libroMock = new Libro(new Isbn("978-1"), "Clean Architecture", "Desc", new Precio(50.0), "url");
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libroMock));
        when(inventarioGateway.obtenerStock("978-1")).thenReturn(Optional.of(new StockDisponible(10)));

        LibroDetalleCompradorDto dto = interactor.verDetalle("978-1");

        assertTrue(dto.isDisponibleParaCompra());
        assertEquals("Clean Architecture", dto.getTitulo());
    }

    @Test
    void debeRetornarLibroNoDisponibleSiStockCero() {
        Libro libroMock = new Libro(new Isbn("978-2"), "Refactoring", "Desc", new Precio(40.0), "url");
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libroMock));
        when(inventarioGateway.obtenerStock("978-2")).thenReturn(Optional.of(new StockDisponible(0)));

        LibroDetalleCompradorDto dto = interactor.verDetalle("978-2");

        assertFalse(dto.isDisponibleParaCompra());
    }

    @Test
    void debeRetornarLibroNoDisponibleSiNoHayRegistroEnInventario() {
        Libro libroMock = new Libro(new Isbn("978-3"), "Domain Driven Design", "Desc", new Precio(60.0), "url");
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libroMock));
        when(inventarioGateway.obtenerStock("978-3")).thenReturn(Optional.empty());

        LibroDetalleCompradorDto dto = interactor.verDetalle("978-3");

        assertFalse(dto.isDisponibleParaCompra());
    }

    @Test
    void debeLanzarExcepcionSiLibroNoExisteEnCatalogo() {
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class, () -> interactor.verDetalle("978-4"));
        verify(inventarioGateway, never()).obtenerStock(anyString());
    }

    @Test
    void debePublicarEventoSiSeProporcionaIdUsuario() {
        Libro libroMock = new Libro(new Isbn("978-5"), "Event Driven", "Desc", new Precio(50.0), "url");
        when(detalleGateway.buscarPorId(any(Isbn.class))).thenReturn(Optional.of(libroMock));
        when(inventarioGateway.obtenerStock("978-5")).thenReturn(Optional.of(new StockDisponible(10)));

        interactor.verDetalle("978-5", "user-123");

        verify(eventPublisher, times(1)).publicar(any(com.openlib.market.domain.historial.LibroVistoEvent.class));
    }
}
