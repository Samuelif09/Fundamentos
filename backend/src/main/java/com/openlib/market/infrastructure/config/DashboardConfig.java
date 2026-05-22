package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.dashboard.IPersonalizarDashboardMetricasUseCase;
import com.openlib.market.application.dashboard.PersonalizarDashboardMetricasInteractor;
import com.openlib.market.domain.dashboard.IConfiguracionAdminGateway;

@Configuration
public class DashboardConfig {

    @Bean
    public IPersonalizarDashboardMetricasUseCase personalizarDashboardMetricasUseCase(
            IConfiguracionAdminGateway configuracionGateway
    ) {
        return new PersonalizarDashboardMetricasInteractor(configuracionGateway);
    }
}