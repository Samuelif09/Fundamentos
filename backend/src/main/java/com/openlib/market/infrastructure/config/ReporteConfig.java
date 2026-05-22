package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.reporte.IExportarDashboardMetricasUseCase;
import com.openlib.market.application.reporte.ExportarDashboardMetricasInteractor;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.reporte.IGeneradorReportesGlobalGateway;

@Configuration
public class ReporteConfig {

    @Bean
    public IExportarDashboardMetricasUseCase exportarDashboardMetricasUseCase(
            IPedidoGateway pedidoGateway,
            IGeneradorReportesGlobalGateway generadorReportes
    ) {
        return new ExportarDashboardMetricasInteractor(pedidoGateway, generadorReportes);
    }
}