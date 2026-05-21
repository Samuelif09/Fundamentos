package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.*;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgregarCarritoInteractorTest {

    private ICarritoGateway carritoGateway;
    private ILibroGateway libroGateway;
    private IInventarioGateway inventarioGateway;
    private AgregarCarritoInteractor interactor;

    @BeforeEach
    void setUp() {
        carritoGateway = mock(ICarritoGateway.class);
        libroGateway = mock(ILibroGateway.class);
        inventarioGateway = mock(IInventarioGateway.class);
        interactor = new AgregarCarritoInteractor(carritoGateway, libroGateway, inventarioGateway);
    }

    @Test
    void debeLanzarExcepcionSiStockEsInsuficiente() {
        when(inventarioGateway.obtenerStock("123")).thenReturn(Optional.of(new StockDisponible(1)));

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-1", "123", 2);

        assertThrows(StockInsuficienteException.class, () -> interactor.agregarAlCarrito(req));
        verify(carritoGateway, never()).guardar(any());
    }

    @Test
    void debeAgregarItemAlCarritoDeUsuarioExitosamente() {
        when(inventarioGateway.obtenerStock("123")).thenReturn(Optional.of(new StockDisponible(5)));
        when(libroGateway.obtenerPorIsbn("123")).thenReturn(Optional.of(new LibroSnapshot("123", 20.0)));
        when(carritoGateway.obtenerPorUsuario(new IdUsuario("user-1"))).thenReturn(Optional.empty());

        AgregarItemRequestDto req = new AgregarItemRequestDto(null, "user-1", "123", 2);

        assertDoesNotThrow(() -> interactor.agregarAlCarrito(req));

        verify(carritoGateway, times(1)).guardar(any(CarritoCompras.class));
    }
}
