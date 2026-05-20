package com.openlib.market.application.reporte;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.domain.reporte.FormatoReporte;
import com.openlib.market.domain.reporte.IGeneradorReportesGlobalGateway;
import com.openlib.market.domain.reporte.ReportePlataforma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportarDashboardMetricasInteractorTest {

    private IPedidoGateway pedidoGateway;
    private IGeneradorReportesGlobalGateway generadorGateway;
    private ExportarDashboardMetricasInteractor interactor;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        generadorGateway = mock(IGeneradorReportesGlobalGateway.class);
        interactor = new ExportarDashboardMetricasInteractor(pedidoGateway, generadorGateway);
    }

    @Test
    void debeExportarReporteVentasLlamandoAlGenerador() {
        Pedido p1 = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        when(pedidoGateway.listarTodos(anyInt(), anyInt())).thenReturn(List.of(p1));
        
        byte[] mockBytes = "file content".getBytes();
        when(generadorGateway.generarReporte(any(ReportePlataforma.class), eq(FormatoReporte.CSV))).thenReturn(mockBytes);

        byte[] result = interactor.exportarReporte("VENTAS", "CSV");

        assertArrayEquals(mockBytes, result);
        verify(pedidoGateway).listarTodos(anyInt(), anyInt());
        verify(generadorGateway).generarReporte(
                argThat(r -> r.getFilas().size() == 1 && r.getFilas().get(0)[2].equals("100.0")),
                eq(FormatoReporte.CSV)
        );
    }
}
