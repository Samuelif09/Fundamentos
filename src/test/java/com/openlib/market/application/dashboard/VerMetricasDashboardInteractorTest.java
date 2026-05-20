package com.openlib.market.application.dashboard;

import com.openlib.market.domain.dashboard.DashboardKpi;
import com.openlib.market.domain.dashboard.IDashboardLibroGateway;
import com.openlib.market.domain.dashboard.IDashboardPedidoGateway;
import com.openlib.market.domain.dashboard.IDashboardUsuarioGateway;
import com.openlib.market.domain.dashboard.Metrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests TDD para A-02: VerMetricasDashboardInteractor.
 * Todos los gateways se mockean con valores estáticos para verificar
 * que el DashboardKpi consolida correctamente sin fallar.
 */
@DisplayName("A-02: VerMetricasDashboardInteractor")
class VerMetricasDashboardInteractorTest {

    private IDashboardUsuarioGateway usuarioGateway;
    private IDashboardPedidoGateway pedidoGateway;
    private IDashboardLibroGateway libroGateway;
    private VerMetricasDashboardInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IDashboardUsuarioGateway.class);
        pedidoGateway = mock(IDashboardPedidoGateway.class);
        libroGateway = mock(IDashboardLibroGateway.class);
        interactor = new VerMetricasDashboardInteractor(usuarioGateway, pedidoGateway, libroGateway);
    }

    @Test
    @DisplayName("Debe consolidar correctamente los KPIs del día con valores mockeados")
    void debeConsolidarKpisCorrectamente() {
        // Arrange: valores estáticos para verificar la orquestación
        when(usuarioGateway.contarNuevosUsuariosHoy()).thenReturn(10L);
        when(usuarioGateway.contarTotalUsuarios()).thenReturn(500L);
        when(pedidoGateway.contarPedidosHoy()).thenReturn(25L);
        when(pedidoGateway.calcularIngresosHoy()).thenReturn(500.0);
        when(pedidoGateway.calcularIngresosTotales()).thenReturn(12500.0);
        when(libroGateway.contarLibrosPendientesAprobacion()).thenReturn(12L);

        // Act
        DashboardKpi kpi = interactor.obtenerKpisDelDia();

        // Assert
        assertNotNull(kpi);
        assertEquals(LocalDate.now(), kpi.getFecha());
        assertEquals(6, kpi.getMetricas().size());
    }

    @Test
    @DisplayName("Las métricas deben tener los valores esperados de los gateways")
    void lasMetricasDebenTenerValoresCorrectos() {
        when(usuarioGateway.contarNuevosUsuariosHoy()).thenReturn(10L);
        when(usuarioGateway.contarTotalUsuarios()).thenReturn(100L);
        when(pedidoGateway.contarPedidosHoy()).thenReturn(5L);
        when(pedidoGateway.calcularIngresosHoy()).thenReturn(250.0);
        when(pedidoGateway.calcularIngresosTotales()).thenReturn(2500.0);
        when(libroGateway.contarLibrosPendientesAprobacion()).thenReturn(8L);

        DashboardKpi kpi = interactor.obtenerKpisDelDia();
        List<Metrica> metricas = kpi.getMetricas();

        // Verificar valor de nuevos usuarios
        Metrica nuevosUsuarios = metricas.get(0);
        assertEquals("Nuevos usuarios hoy", nuevosUsuarios.getNombre());
        assertEquals(10.0, nuevosUsuarios.getValor());

        // Verificar ingresos del día
        Metrica ingresosHoy = metricas.get(3);
        assertEquals("Ingresos hoy (USD)", ingresosHoy.getNombre());
        assertEquals(250.0, ingresosHoy.getValor());

        // Verificar libros pendientes
        Metrica librosPendientes = metricas.get(5);
        assertEquals("Libros pendientes", librosPendientes.getNombre());
        assertEquals(8.0, librosPendientes.getValor());
    }

    @Test
    @DisplayName("Debe funcionar correctamente si no hay pedidos ni usuarios hoy (valores cero)")
    void debeManejarValoresCeroSinFallar() {
        when(usuarioGateway.contarNuevosUsuariosHoy()).thenReturn(0L);
        when(usuarioGateway.contarTotalUsuarios()).thenReturn(0L);
        when(pedidoGateway.contarPedidosHoy()).thenReturn(0L);
        when(pedidoGateway.calcularIngresosHoy()).thenReturn(0.0);
        when(pedidoGateway.calcularIngresosTotales()).thenReturn(0.0);
        when(libroGateway.contarLibrosPendientesAprobacion()).thenReturn(0L);

        assertDoesNotThrow(() -> interactor.obtenerKpisDelDia());
    }

    @Test
    @DisplayName("Todos los gateways deben ser consultados exactamente una vez")
    void debeLlamarATodosLosGateways() {
        when(usuarioGateway.contarNuevosUsuariosHoy()).thenReturn(5L);
        when(usuarioGateway.contarTotalUsuarios()).thenReturn(50L);
        when(pedidoGateway.contarPedidosHoy()).thenReturn(3L);
        when(pedidoGateway.calcularIngresosHoy()).thenReturn(100.0);
        when(pedidoGateway.calcularIngresosTotales()).thenReturn(1000.0);
        when(libroGateway.contarLibrosPendientesAprobacion()).thenReturn(2L);

        interactor.obtenerKpisDelDia();

        verify(usuarioGateway, times(1)).contarNuevosUsuariosHoy();
        verify(usuarioGateway, times(1)).contarTotalUsuarios();
        verify(pedidoGateway, times(1)).contarPedidosHoy();
        verify(pedidoGateway, times(1)).calcularIngresosHoy();
        verify(pedidoGateway, times(1)).calcularIngresosTotales();
        verify(libroGateway, times(1)).contarLibrosPendientesAprobacion();
    }
}

