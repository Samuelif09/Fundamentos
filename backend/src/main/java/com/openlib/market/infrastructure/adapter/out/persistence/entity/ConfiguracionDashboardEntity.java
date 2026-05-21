package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "configuracion_dashboard")
public class ConfiguracionDashboardEntity {

    @Id
    private String idAdmin;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dashboard_widgets", joinColumns = @JoinColumn(name = "config_id"))
    private List<WidgetEmbeddable> widgets = new ArrayList<>();

    public ConfiguracionDashboardEntity() {}

    public ConfiguracionDashboardEntity(String idAdmin, List<WidgetEmbeddable> widgets) {
        this.idAdmin = idAdmin;
        this.widgets = widgets != null ? widgets : new ArrayList<>();
    }

    public String getIdAdmin() { return idAdmin; }
    public void setIdAdmin(String idAdmin) { this.idAdmin = idAdmin; }

    public List<WidgetEmbeddable> getWidgets() { return widgets; }
    public void setWidgets(List<WidgetEmbeddable> widgets) { this.widgets = widgets; }
}
