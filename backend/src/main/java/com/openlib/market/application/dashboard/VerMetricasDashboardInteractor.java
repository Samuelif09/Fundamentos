package com.openlib.market.application.dashboard;

import com.openlib.market.domain.dashboard.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Caso de uso A-02: Ver KPIs del día en el dashboard de administración.
 *
 * Actúa como "Orquestador de Lectura Global" (patrón Facade):
 * inyecta múltiples gateways, compila la foto del día y la empaqueta
 * en un DashboardKpi sin acoplar los módulos entre sí.
 */
public class VerMetricasDashboardInteractor implements IVerMetricasDashboardUseCase {

    private final IDashboardUsuarioGateway usuarioGateway;
    private final IDashboardPedidoGateway pedidoGateway;
    private final IDashboardLibroGateway libroGateway;

    public VerMetricasDashboardInteractor(IDashboardUsuarioGateway usuarioGateway,
                                          IDashboardPedidoGateway pedidoGateway,
                                          IDashboardLibroGateway libroGateway) {
        this.usuarioGateway = usuarioGateway;
        this.pedidoGateway = pedidoGateway;
        this.libroGateway = libroGateway;
    }

    @Override
    public DashboardKpi obtenerKpisDelDia() {
        // Orquestación de lecturas: cada gateway es independiente
        long nuevosUsuarios = usuarioGateway.contarNuevosUsuariosHoy();
        long totalUsuarios = usuarioGateway.contarTotalUsuarios();
        long pedidosHoy = pedidoGateway.contarPedidosHoy();
        double ingresosHoy = pedidoGateway.calcularIngresosHoy();
        double ingresosTotales = pedidoGateway.calcularIngresosTotales();
        long librosPendientes = libroGateway.contarLibrosPendientesAprobacion();

        // Construir las métricas del dashboard
        List<Metrica> metricas = List.of(
                new Metrica("Nuevos usuarios hoy", nuevosUsuarios, calcularVariacion(nuevosUsuarios, totalUsuarios)),
                new Metrica("Total usuarios", totalUsuarios, 0.0),
                new Metrica("Pedidos hoy", pedidosHoy, 0.0),
                new Metrica("Ingresos hoy (USD)", ingresosHoy, calcularVariacion(ingresosHoy, ingresosTotales)),
                new Metrica("Ingresos totales (USD)", ingresosTotales, 0.0),
                new Metrica("Libros pendientes", librosPendientes, 0.0)
        );

        return new DashboardKpi(LocalDate.now(), metricas);
    }

    private double calcularVariacion(double valor, double base) {
        if (base <= 0) return 0.0;
        return Math.round(((valor / base) * 100.0) * 10.0) / 10.0;
    }
}
