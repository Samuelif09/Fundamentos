package com.openlib.market.domain.dashboard;

/**
 * Puerto de salida para consultas de usuarios.
 * Expone métodos de conteo diario necesarios para el dashboard.
 */
public interface IDashboardUsuarioGateway {
    long contarNuevosUsuariosHoy();
    long contarTotalUsuarios();
}
