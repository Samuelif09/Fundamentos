package com.openlib.market.domain.dashboard;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionDashboard {
    private final String idAdmin;
    private final List<Widget> widgets;

    public ConfiguracionDashboard(String idAdmin) {
        if (idAdmin == null || idAdmin.trim().isEmpty()) {
            throw new IllegalArgumentException("El id del administrador es obligatorio");
        }
        this.idAdmin = idAdmin;
        this.widgets = new ArrayList<>();
    }

    public ConfiguracionDashboard(String idAdmin, List<Widget> widgets) {
        if (idAdmin == null || idAdmin.trim().isEmpty()) {
            throw new IllegalArgumentException("El id del administrador es obligatorio");
        }
        this.idAdmin = idAdmin;
        this.widgets = widgets != null ? new ArrayList<>(widgets) : new ArrayList<>();
    }

    public String getIdAdmin() { return idAdmin; }
    public List<Widget> getWidgets() { return List.copyOf(widgets); }

    public void actualizarWidgets(List<Widget> nuevosWidgets) {
        if (nuevosWidgets == null) {
            throw new IllegalArgumentException("La lista de widgets no puede ser nula");
        }
        
        // Aquí se podrían agregar validaciones de dominio más complejas
        // como evitar que dos widgets se superpongan si es una regla estricta de negocio,
        // pero para este MVP, simplemente reemplazamos la lista.
        
        this.widgets.clear();
        this.widgets.addAll(nuevosWidgets);
    }
}
