package com.openlib.market.application.cupon;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;
import com.openlib.market.domain.cupon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AplicarCuponInteractorTest {

    private ICuponGateway cuponGateway;
    private ICarritoGateway carritoGateway;
    private AplicarCuponInteractor interactor;

    @BeforeEach
    void setUp() {
        cuponGateway = mock(ICuponGateway.class);
        carritoGateway = mock(ICarritoGateway.class);
        interactor = new AplicarCuponInteractor(cuponGateway, carritoGateway);
    }

    @Test
    void debeAplicarCuponExitosamente() {
        AplicarCuponRequestDto request = new AplicarCuponRequestDto("u-1", "DESC10");

        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("DESC10"),
                new DescuentoPorcentaje(10),
                LocalDate.now().plusDays(10)
        );
        when(cuponGateway.buscarPorCodigo(any(CodigoCupon.class))).thenReturn(Optional.of(cupon));

        CarritoCompras carrito = mock(CarritoCompras.class);
        when(carrito.getTotal()).thenReturn(100.0).thenReturn(90.0); // original then discounted
        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.of(carrito));

        AplicarCuponResponseDto response = interactor.aplicar(request);

        assertNotNull(response);
        assertEquals(100.0, response.getTotalOriginal());
        assertEquals(90.0, response.getTotalConDescuento());
        assertEquals("DESC10", response.getCodigoCupon());

        verify(carrito).aplicarDescuento(cupon);
        verify(carritoGateway).guardar(carrito);
    }

    @Test
    void debeLanzarExcepcionSiCuponNoExiste() {
        AplicarCuponRequestDto request = new AplicarCuponRequestDto("u-1", "INVALIDO");
        when(cuponGateway.buscarPorCodigo(any(CodigoCupon.class))).thenReturn(Optional.empty());

        assertThrows(CuponNoEncontradoException.class, () -> interactor.aplicar(request));
        verify(carritoGateway, never()).guardar(any());
    }

    @Test
    void debeLanzarExcepcionSiCuponExpirado() {
        AplicarCuponRequestDto request = new AplicarCuponRequestDto("u-1", "VENCIDO");
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("VENCIDO"),
                new DescuentoPorcentaje(10),
                LocalDate.now().minusDays(1)
        );
        when(cuponGateway.buscarPorCodigo(any(CodigoCupon.class))).thenReturn(Optional.of(cupon));

        assertThrows(CuponExpiradoException.class, () -> interactor.aplicar(request));
        verify(carritoGateway, never()).obtenerPorUsuario(any());
    }

    @Test
    void debeLanzarExcepcionSiCarritoNoExiste() {
        AplicarCuponRequestDto request = new AplicarCuponRequestDto("u-1", "DESC10");
        CuponDescuento cupon = new CuponDescuento(
                new CodigoCupon("DESC10"),
                new DescuentoPorcentaje(10),
                LocalDate.now().plusDays(10)
        );
        when(cuponGateway.buscarPorCodigo(any(CodigoCupon.class))).thenReturn(Optional.of(cupon));
        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> interactor.aplicar(request));
    }
}
