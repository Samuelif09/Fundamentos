package com.openlib.market.application.reporte;

import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.reporte.*;

import java.util.List;
import java.util.stream.Collectors;

public class ExportarDashboardMetricasInteractor implements IExportarDashboardMetricasUseCase {

    private final IPedidoGateway pedidoGateway;
    private final IGeneradorReportesGlobalGateway generadorReportes;

    public ExportarDashboardMetricasInteractor(IPedidoGateway pedidoGateway, IGeneradorReportesGlobalGateway generadorReportes) {
        this.pedidoGateway = pedidoGateway;
        this.generadorReportes = generadorReportes;
    }

    @Override
    public byte[] exportarReporte(String tipoStr, String formatoStr) {
        TipoReporte tipo = TipoReporte.valueOf(tipoStr.toUpperCase());
        FormatoReporte formato = FormatoReporte.valueOf(formatoStr.toUpperCase());

        ReportePlataforma reporte;

        if (tipo == TipoReporte.VENTAS) {
            List<Pedido> pedidos = pedidoGateway.listarTodos(0, 10000); // Todos para el reporte
            String[] cabeceras = {"ID Pedido", "Usuario", "Monto", "Estado", "Fecha"};
            List<String[]> filas = pedidos.stream().map(p -> new String[]{
                    p.getId(),
                    p.getIdUsuario(),
                    String.valueOf(p.getTotal()),
                    p.getEstado().name(),
                    p.getFecha().toString()
            }).collect(Collectors.toList());

            reporte = new ReportePlataforma(tipo, "Reporte Global de Ventas", cabeceras, filas);
        } else {
            // Mock de otros reportes para entrega 1
            reporte = new ReportePlataforma(tipo, "Reporte de " + tipo.name(), new String[]{"Col1"}, List.of());
        }

        return generadorReportes.generarReporte(reporte, formato);
    }
}
