package com.openlib.market.infrastructure.config;

import com.openlib.market.application.soporte.IReportarSoporteUseCase;
import com.openlib.market.application.soporte.ReportarSoporteInteractor;
import com.openlib.market.domain.soporte.IReporteGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoporteConfig {

    @Bean
    public IReportarSoporteUseCase reportarSoporteUseCase(IReporteGateway reporteGateway) {
        return new ReportarSoporteInteractor(reporteGateway);
    }
}
