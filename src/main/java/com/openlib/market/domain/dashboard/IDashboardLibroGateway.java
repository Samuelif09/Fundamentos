package com.openlib.market.domain.dashboard;

/**
 * Puerto de salida para consultas de libros en el dashboard.
 * Expone el conteo de libros pendientes de aprobación (AC-001 A-02).
 */
public interface IDashboardLibroGateway {
    long contarLibrosPendientesAprobacion();
    long contarTotalLibros();
}
