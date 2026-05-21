package com.openlib.market.application.dashboard;

import com.openlib.market.domain.dashboard.DashboardKpi;

/**
 * Puerto de entrada (Use Case) para A-02: Ver KPIs del día en el dashboard.
 */
public interface IVerMetricasDashboardUseCase {
    DashboardKpi obtenerKpisDelDia();
}
