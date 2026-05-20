package com.openlib.market.application.curaduria;

import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuraduriaInteractorsTest {

    private ICuraduriaGateway curaduriaGateway;
    private IVendedorGateway vendedorGateway;
    private INotificacionGateway notificacionGateway;
    private RevisarCuraduriaContenidoInteractor revisarInteractor;
    private RechazarCuraduriaContenidoInteractor rechazarInteractor;

    @BeforeEach
    void setUp() {
        curaduriaGateway = mock(ICuraduriaGateway.class);
        vendedorGateway = mock(IVendedorGateway.class);
        notificacionGateway = mock(INotificacionGateway.class);
        revisarInteractor = new RevisarCuraduriaContenidoInteractor(curaduriaGateway, vendedorGateway);
        rechazarInteractor = new RechazarCuraduriaContenidoInteractor(curaduriaGateway, notificacionGateway);
    }

    @Test
    void debeRetornarLibrosEnRevisionConDatosDeVendedor() {
        Libro libro = new Libro(new Isbn("isbn1"), "Titulo", "Sinopsis", new Precio(10.0), "url", "cat", "seller1", EstadoLibro.EN_REVISION, null);
        when(curaduriaGateway.listarPorEstado(EstadoLibro.EN_REVISION, 0, 10)).thenReturn(List.of(libro));
        when(vendedorGateway.obtenerPorId("seller1")).thenReturn(Optional.of(new Vendedor("seller1", "user1", new RazonSocial("Mi Tienda"), new IdentificacionTributaria("12345"))));

        List<LibroParaRevisionDto> result = revisarInteractor.listarLibrosPendientes(0, 10);

        assertEquals(1, result.size());
        assertEquals("Mi Tienda", result.get(0).getNombreVendedor());
        assertEquals("12345", result.get(0).getIdentificacionTributariaVendedor());
    }

    @Test
    void debeRechazarLibroYNotificarAlVendedor() {
        Libro libro = new Libro(new Isbn("isbn1"), "Titulo", "Sinopsis", new Precio(10.0), "url", "cat", "seller1", EstadoLibro.EN_REVISION, null);
        when(curaduriaGateway.obtenerPorIsbn("isbn1")).thenReturn(Optional.of(libro));

        rechazarInteractor.rechazarLibro("isbn1", "El libro contiene errores ortográficos graves.");

        verify(curaduriaGateway).actualizar(argThat(l -> l.getEstado() == EstadoLibro.RECHAZADO));
        verify(notificacionGateway).notificarRechazoLibro(eq("seller1"), eq("Titulo"), eq("El libro contiene errores ortográficos graves."));
    }
}
