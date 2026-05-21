package com.openlib.market.application.estadisticas;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.estadisticas.EstadisticaLector;
import com.openlib.market.domain.historial.HistorialNavegacion;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.historial.ItemNavegacion;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerEstadisticasMiCuentaInteractorTest {

    private IPedidoGateway pedidoGateway;
    private IHistorialNavegacionGateway historialGateway;
    private IDetalleGateway detalleGateway;
    private VerEstadisticasMiCuentaInteractor interactor;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        historialGateway = mock(IHistorialNavegacionGateway.class);
        detalleGateway = mock(IDetalleGateway.class);
        interactor = new VerEstadisticasMiCuentaInteractor(pedidoGateway, historialGateway, detalleGateway);
    }

    @Test
    void debeRetornarEstadisticasConCategoriaFavorita() {
        when(pedidoGateway.listarPorUsuarioId("user1", 0, 1000)).thenReturn(List.of(mock(Pedido.class), mock(Pedido.class)));

        HistorialNavegacion historial = new HistorialNavegacion("user1", List.of(
                new ItemNavegacion("isbn1", LocalDateTime.now()),
                new ItemNavegacion("isbn2", LocalDateTime.now()),
                new ItemNavegacion("isbn3", LocalDateTime.now())
        ));
        when(historialGateway.obtenerPorUsuario("user1")).thenReturn(Optional.of(historial));

        Libro libroFiccion1 = mock(Libro.class);
        when(libroFiccion1.getCategoria()).thenReturn("Ficción");
        
        Libro libroFiccion2 = mock(Libro.class);
        when(libroFiccion2.getCategoria()).thenReturn("Ficción");

        Libro libroTerror = mock(Libro.class);
        when(libroTerror.getCategoria()).thenReturn("Terror");

        when(detalleGateway.buscarPorId(new Isbn("isbn1"))).thenReturn(Optional.of(libroFiccion1));
        when(detalleGateway.buscarPorId(new Isbn("isbn2"))).thenReturn(Optional.of(libroFiccion2));
        when(detalleGateway.buscarPorId(new Isbn("isbn3"))).thenReturn(Optional.of(libroTerror));

        EstadisticaLector stats = interactor.obtenerEstadisticas("user1");

        assertEquals(2, stats.getTotalPedidosRealizados());
        assertEquals("Ficción", stats.getCategoriaFavorita());
        assertEquals("user1", stats.getIdUsuario());
    }

    @Test
    void debeRetornarNingunaCategoriaSiNoHayHistorial() {
        when(pedidoGateway.listarPorUsuarioId("user2", 0, 1000)).thenReturn(List.of());
        when(historialGateway.obtenerPorUsuario("user2")).thenReturn(Optional.empty());

        EstadisticaLector stats = interactor.obtenerEstadisticas("user2");

        assertEquals(0, stats.getTotalPedidosRealizados());
        assertEquals("Ninguna", stats.getCategoriaFavorita());
    }
}
