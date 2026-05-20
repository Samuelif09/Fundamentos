package com.openlib.market.domain.dashboard;

import java.time.LocalDate;
import java.util.List;

/**
 * Proyección de lectura (Read Model / DTO de dominio) para A-02.
 * Consolida todos los KPIs del día sin lógica de negocio transaccional.
 * Es el objeto que el Interactor construye y el Controller serializa.
 */
public class DashboardKpi {

    private final LocalDate fecha;
    private final List<Metrica> metricas;

    public DashboardKpi(LocalDate fecha, List<Metrica> metricas) {
        this.fecha = fecha;
        this.metricas = List.copyOf(metricas);
    }

    public LocalDate getFecha() { return fecha; }
    public List<Metrica> getMetricas() { return metricas; }
}
