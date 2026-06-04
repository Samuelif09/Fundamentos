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
import com.openlib.market.application.configuracion.IGestionarConfiguracionSistemaUseCase;
import com.openlib.market.application.configuracion.GestionarConfiguracionSistemaInteractor;
import com.openlib.market.domain.configuracion.IMetodoPagoConfigGateway;
import com.openlib.market.application.configuracion.IConfigurarComisionesUseCase;
import com.openlib.market.application.configuracion.ConfigurarComisionesInteractor;
import com.openlib.market.domain.configuracion.IConfiguracionComisionGateway;
import com.openlib.market.application.categoria.IGestionarCategoriasUseCase;
import com.openlib.market.application.categoria.GestionarCategoriasInteractor;
import com.openlib.market.domain.categoria.ICategoriaGateway;
import com.openlib.market.application.dashboardGlobal.IVerDashboardMetricasUseCase;
import com.openlib.market.application.dashboardGlobal.VerDashboardMetricasInteractor;
import com.openlib.market.domain.dashboardGlobal.IDashboardGlobalGateway;
import com.openlib.market.application.transaccionesAdmin.IVerTransaccionesAdminUseCase;
import com.openlib.market.application.transaccionesAdmin.VerTransaccionesAdminInteractor;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.application.reembolso.IGestionarReembolsosUseCase;
import com.openlib.market.application.reembolso.GestionarReembolsosInteractor;
import com.openlib.market.domain.reembolso.IReembolsoGateway;
import com.openlib.market.domain.reembolso.IPasarelaPagoGateway;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.application.soporte.IVerSoporteUseCase;
import com.openlib.market.application.soporte.VerSoporteInteractor;
import com.openlib.market.domain.soporte.ITicketSoporteGateway;

/**
 * Configuración de beans para las historias A-01 y A-02 del panel de administración.
 */
@Configuration
public class AdminConfig {

    // ── A-01: Autenticación Admin ──────────────────────────────────────────────
    @Bean
    public IIniciarAutenticacionAdminUseCase iniciarAutenticacionAdminUseCase(
            com.openlib.market.domain.autenticacion.IUsuarioAuthGateway usuarioGateway,
            IVerificadorPasswordGateway verificadorPassword,
            ITokenGeneratorGateway tokenGenerator) {
        return new IniciarAutenticacionAdminInteractor(usuarioGateway, verificadorPassword, tokenGenerator);
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
    @Bean
    public IGestionarConfiguracionSistemaUseCase gestionarConfiguracionSistemaUseCase(
            IMetodoPagoConfigGateway configGateway) {

        return new GestionarConfiguracionSistemaInteractor(configGateway);
    }

    @Bean
    public IConfigurarComisionesUseCase configurarComisionesUseCase(
            IConfiguracionComisionGateway comisionGateway) {

        return new ConfigurarComisionesInteractor(comisionGateway);
    }

    @Bean
    public IGestionarCategoriasUseCase gestionarCategoriasUseCase(
            ICategoriaGateway categoriaGateway) {

        return new GestionarCategoriasInteractor(categoriaGateway);
    }

    @Bean
    public IVerDashboardMetricasUseCase verDashboardMetricasUseCase(
            IDashboardGlobalGateway dashboardGateway) {

        return new VerDashboardMetricasInteractor(dashboardGateway);
    }

    @Bean
    public IVerTransaccionesAdminUseCase verTransaccionesAdminUseCase(
            IPedidoGateway pedidoGateway) {

        return new VerTransaccionesAdminInteractor(pedidoGateway);
    }

    @Bean
    public IGestionarReembolsosUseCase gestionarReembolsosUseCase(
            IReembolsoGateway reembolsoGateway,
            IPedidoGateway pedidoGateway,
            IPasarelaPagoGateway pasarelaPagoGateway) {

        return new GestionarReembolsosInteractor(reembolsoGateway, pedidoGateway, pasarelaPagoGateway);
    }

    @Bean
    public IVerSoporteUseCase verSoporteUseCase(ITicketSoporteGateway ticketGateway) {
        return new VerSoporteInteractor(ticketGateway);
    }
}

