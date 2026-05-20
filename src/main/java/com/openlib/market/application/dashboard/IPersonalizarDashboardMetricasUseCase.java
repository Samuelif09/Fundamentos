package com.openlib.market.application.dashboard;

import java.util.List;

public interface IPersonalizarDashboardMetricasUseCase {
    ConfiguracionDashboardDto obtenerPreferencias(String idAdmin);
    ConfiguracionDashboardDto guardarPreferencias(String idAdmin, List<WidgetDto> widgets);
}
