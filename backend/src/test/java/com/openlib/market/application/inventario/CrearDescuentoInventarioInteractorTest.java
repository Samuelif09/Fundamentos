package com.openlib.market.application.inventario;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.inventario.IPromocionGateway;
import com.openlib.market.domain.inventario.PeriodoPromocion;
import com.openlib.market.domain.inventario.PorcentajeDescuento;
import com.openlib.market.domain.inventario.PromocionLibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CrearDescuentoInventarioInteractorTest {

    private ILibroPublicacionGateway libroGateway;
    private IPromocionGateway promocionGateway;
    private CrearDescuentoInventarioInteractor interactor;

    @BeforeEach
    void setUp() {
        libroGateway = mock(ILibroPublicacionGateway.class);
        promocionGateway = mock(IPromocionGateway.class);
        interactor = new CrearDescuentoInventarioInteractor(libroGateway, promocionGateway);
    }

    @Test
    void debeGuardarPromocionSiNoHaySolapamientos() {
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(
                new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1")
        ));
        when(promocionGateway.obtenerPorIsbn("isbn-1")).thenReturn(List.of());

        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fin = inicio.plusDays(5);
        
        interactor.crearDescuento("seller-1", "isbn-1", 20, inicio, fin);

        verify(promocionGateway).guardar(any(PromocionLibro.class));
    }

    @Test
    void debeLanzarExcepcionSiSolapaConOtraPromocion() {
        when(libroGateway.obtenerPorIsbn("isbn-1")).thenReturn(Optional.of(
                new Libro(new Isbn("isbn-1"), "T", "S", new Precio(10), "U", "C", "seller-1")
        ));
        
        LocalDate inicioExistente = LocalDate.now().plusDays(5);
        LocalDate finExistente = inicioExistente.plusDays(5);
        PromocionLibro promoActiva = new PromocionLibro("isbn-1", new PorcentajeDescuento(10), new PeriodoPromocion(inicioExistente, finExistente));
        
        when(promocionGateway.obtenerPorIsbn("isbn-1")).thenReturn(List.of(promoActiva));

        // Intento crear una promo del día 1 al 6 (se solapa en el día 5 y 6)
        LocalDate inicioNuevo = LocalDate.now().plusDays(1);
        LocalDate finNuevo = LocalDate.now().plusDays(6);
        
        assertThrows(IllegalStateException.class, () -> 
            interactor.crearDescuento("seller-1", "isbn-1", 15, inicioNuevo, finNuevo)
        );
        verify(promocionGateway, never()).guardar(any());
    }
}
