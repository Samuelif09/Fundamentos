package com.openlib.market.infrastructure.config;

import com.openlib.market.application.autenticacion.IIniciarAutenticacionAdminUseCase;
import com.openlib.market.application.autenticacion.IniciarAutenticacionAdminInteractor;
import com.openlib.market.application.dashboard.IVerMetricasDashboardUseCase;
import com.openlib.market.application.dashboard.VerMetricasDashboardInteractor;
import com.openlib.market.application.gestionUsuarios.ISuspenderGestionUsuariosUseCase;
import com.openlib.market.application.gestionUsuarios.SuspenderGestionUsuariosInteractor;
import com.openlib.market.domain.autenticacion.IAdminGateway;
import com.openlib.market.domain.autenticacion.ITokenGeneratorGateway;
import com.openlib.market.domain.autenticacion.IVerificadorPasswordGateway;
import com.openlib.market.domain.dashboard.IDashboardLibroGateway;
import com.openlib.market.domain.dashboard.IDashboardPedidoGateway;
import com.openlib.market.domain.dashboard.IDashboardUsuarioGateway;
import com.openlib.market.domain.gestionUsuarios.INotificacionGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans para las historias A-01 y A-02 del panel de administración.
 */
@Configuration
public class AdminConfig {

    // ── A-01: Autenticación Admin ──────────────────────────────────────────────
    @Bean
    public IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase(
            IAdminGateway adminGateway,
            IVerificadorPasswordGateway verificadorPassword,
            ITokenGeneratorGateway tokenGenerator) {
        return new IniciarAutenticacionAdminInteractor(adminGateway, verificadorPassword, tokenGenerator);
    }

    // ── A-02: Dashboard KPIs ───────────────────────────────────────────────────
    @Bean
    public IVerMetricasDashboardUseCase verMetricasDashboardUseCase(
            IDashboardUsuarioGateway usuarioGateway,
            IDashboardPedidoGateway pedidoGateway,
            IDashboardLibroGateway libroGateway) {
        return new VerMetricasDashboardInteractor(usuarioGateway, pedidoGateway, libroGateway);
    }

    // ── A-03: Suspender Usuario ────────────────────────────────────────────────
    @Bean
    public ISuspenderGestionUsuariosUseCase suspenderGestionUsuariosUseCase(
            IUsuarioGateway gestionUsuariosJsonGateway,
            INotificacionGateway notificacionGateway) {
        return new SuspenderGestionUsuariosInteractor(gestionUsuariosJsonGateway, notificacionGateway);
    }
}

