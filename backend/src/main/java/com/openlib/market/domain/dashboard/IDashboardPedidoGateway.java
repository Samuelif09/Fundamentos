package com.openlib.market.domain.dashboard;

/**
 * Puerto de salida para consultas de pedidos/ventas.
 * Expone métricas de ingresos y pedidos del día.
 */
public interface IDashboardPedidoGateway {
    long contarPedidosHoy();
    double calcularIngresosHoy();
    double calcularIngresosTotales();
}
