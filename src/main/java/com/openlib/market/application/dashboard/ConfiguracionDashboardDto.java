package com.openlib.market.application.dashboard;

import java.util.List;

public class ConfiguracionDashboardDto {
    private final String idAdmin;
    private final List<WidgetDto> widgets;

    public ConfiguracionDashboardDto(String idAdmin, List<WidgetDto> widgets) {
        this.idAdmin = idAdmin;
        this.widgets = widgets;
    }

    public String getIdAdmin() { return idAdmin; }
    public List<WidgetDto> getWidgets() { return widgets; }
}
