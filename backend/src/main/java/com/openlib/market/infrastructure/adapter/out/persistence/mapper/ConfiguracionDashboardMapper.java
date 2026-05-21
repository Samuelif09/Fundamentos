package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.dashboard.ConfiguracionDashboard;
import com.openlib.market.domain.dashboard.Posicion;
import com.openlib.market.domain.dashboard.Tamano;
import com.openlib.market.domain.dashboard.TipoWidget;
import com.openlib.market.domain.dashboard.Widget;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ConfiguracionDashboardEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.WidgetEmbeddable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfiguracionDashboardMapper {

    public ConfiguracionDashboard toDomain(ConfiguracionDashboardEntity entity) {
        List<Widget> widgets = entity.getWidgets().stream()
                .map(w -> new Widget(
                        TipoWidget.valueOf(w.getTipo()),
                        new Posicion(w.getPosicionX(), w.getPosicionY()),
                        new Tamano(w.getTamanoAncho(), w.getTamanoAlto())
                ))
                .collect(Collectors.toList());

        return new ConfiguracionDashboard(entity.getIdAdmin(), widgets);
    }

    public ConfiguracionDashboardEntity toEntity(ConfiguracionDashboard domain) {
        List<WidgetEmbeddable> widgets = domain.getWidgets().stream()
                .map(w -> new WidgetEmbeddable(
                        w.getTipo().name(),
                        w.getPosicion().x(),
                        w.getPosicion().y(),
                        w.getTamano().ancho(),
                        w.getTamano().alto()
                ))
                .collect(Collectors.toList());

        return new ConfiguracionDashboardEntity(domain.getIdAdmin(), widgets);
    }
}
