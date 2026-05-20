package com.openlib.market.infrastructure.config;

import com.openlib.market.application.finanzas.IVerFinanzasUseCase;
import com.openlib.market.application.finanzas.VerFinanzasInteractor;
import com.openlib.market.domain.finanzas.CalculadoraIngresosDomainService;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.ReglaComisionDomainService;
import com.openlib.market.domain.finanzas.IGeneradorReportesGateway;
import com.openlib.market.domain.finanzas.IBilleteraGateway;
import com.openlib.market.domain.finanzas.IRetiroGateway;
import com.openlib.market.domain.finanzas.IFacturacionGateway;
import com.openlib.market.application.finanzas.IGenerarFacturaFinanzasUseCase;
import com.openlib.market.application.finanzas.GenerarFacturaFinanzasInteractor;
import com.openlib.market.application.finanzas.DescargarFacturaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinanzasConfig {

    @Bean
    public CalculadoraIngresosDomainService calculadoraIngresosDomainService() {
        return new CalculadoraIngresosDomainService();
    }

    @Bean
    public IVerFinanzasUseCase verFinanzasUseCase(
            ILiquidacionGateway liquidacionGateway,
            CalculadoraIngresosDomainService calculadoraIngresosDomainService) {
        return new VerFinanzasInteractor(liquidacionGateway, calculadoraIngresosDomainService);
    }

    @Bean
    public ReglaComisionDomainService reglaComisionDomainService() {
        return new ReglaComisionDomainService();
    }

    @Bean
    public com.openlib.market.application.finanzas.IVerDesgloseFinanzasUseCase verDesgloseUseCase(
            ILiquidacionGateway liquidacionGateway,
            ReglaComisionDomainService reglaComisionDomainService) {
        return new com.openlib.market.application.finanzas.VerDesgloseFinanzasInteractor(liquidacionGateway, reglaComisionDomainService);
    }

    @Bean
    public com.openlib.market.application.finanzas.IExportarVentasUseCase exportarVentasUseCase(
            ILiquidacionGateway liquidacionGateway,
            IGeneradorReportesGateway generadorReportesGateway) {
        return new com.openlib.market.application.finanzas.ExportarVentasInteractor(liquidacionGateway, generadorReportesGateway);
    }

    @Bean
    public com.openlib.market.application.finanzas.IVerGraficasVentasUseCase verGraficasVentasUseCase(
            ILiquidacionGateway liquidacionGateway) {
        return new com.openlib.market.application.finanzas.VerGraficasVentasInteractor(liquidacionGateway);
    }

    @Bean
    public com.openlib.market.application.finanzas.ISolicitarRetiroFinanzasUseCase solicitarRetiroFinanzasUseCase(
            IBilleteraGateway billeteraGateway,
            IRetiroGateway retiroGateway) {
        return new com.openlib.market.application.finanzas.SolicitarRetiroFinanzasInteractor(billeteraGateway, retiroGateway);
    }

    @Bean
    public IGenerarFacturaFinanzasUseCase generarFacturaFinanzasUseCase(IFacturacionGateway facturacionGateway) {
        return new GenerarFacturaFinanzasInteractor(facturacionGateway);
    }

    @Bean
    public DescargarFacturaUseCase descargarFacturaUseCase(IFacturacionGateway facturacionGateway) {
        return new DescargarFacturaUseCase(facturacionGateway);
    }
}
