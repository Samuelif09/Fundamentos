package com.openlib.market.application.carrito;

import com.openlib.market.domain.carrito.Cantidad;
import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;
import com.openlib.market.domain.carrito.LibroSnapshot;
import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerCarritoInteractorTest {

    private ICarritoGateway carritoGateway;
    private IContenidoDigitalGateway contenidoGateway;
    private VerCarritoInteractor interactor;

    @BeforeEach
    void setUp() {
        carritoGateway = mock(ICarritoGateway.class);
        contenidoGateway = mock(IContenidoDigitalGateway.class);
        interactor = new VerCarritoInteractor(carritoGateway, contenidoGateway);
    }

    @Test
    void debeRetornarCarritoVacioCuandoNoExiste() {
        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.empty());

        CarritoResponseDto dto = interactor.verCarritoUsuario("user-1");

        assertEquals("user-1", dto.getSesionId());
        assertTrue(dto.getItems().isEmpty());
        assertEquals(0.0, dto.getTotal());
    }

    @Test
    void debeRetornarCarritoConTotalDecorado() {
        CarritoCompras carrito = new CarritoCompras(new IdUsuario("user-1"));
        carrito.agregarItem(new LibroSnapshot("isbn-1", 100.0), new Cantidad(2)); // Subtotal = 200.0
        
        ContenidoDigital contenido = mock(ContenidoDigital.class);
        when(contenido.getTitulo()).thenReturn("Libro Test");

        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.of(carrito));
        when(contenidoGateway.obtenerContenidoPorId("isbn-1")).thenReturn(Optional.of(contenido));

        CarritoResponseDto dto = interactor.verCarritoUsuario("user-1");

        assertEquals("user-1", dto.getSesionId());
        assertEquals(1, dto.getItems().size());
        
        CarritoItemDto itemDto = dto.getItems().get(0);
        assertEquals("Libro Test", itemDto.getNombreProducto());
        assertEquals(2, itemDto.getCantidad());
        assertEquals(100.0, itemDto.getPrecioUnitario());

        // Total = 200 + 19% = 238.0
        assertEquals(238.0, dto.getTotal(), 0.01);
    }
}
