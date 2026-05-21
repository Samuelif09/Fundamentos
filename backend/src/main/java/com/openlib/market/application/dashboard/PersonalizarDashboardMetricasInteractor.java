package com.openlib.market.application.dashboard;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.dashboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalizarDashboardMetricasInteractor implements IPersonalizarDashboardMetricasUseCase {

    private final IConfiguracionAdminGateway configuracionGateway;

    public PersonalizarDashboardMetricasInteractor(IConfiguracionAdminGateway configuracionGateway) {
        this.configuracionGateway = configuracionGateway;
    }

    @Override
    public ConfiguracionDashboardDto obtenerPreferencias(String idAdmin) {
        ConfiguracionDashboard config = configuracionGateway.buscarPorAdminId(idAdmin)
                .orElse(new ConfiguracionDashboard(idAdmin));

        return toDto(config);
    }

    @Override
    public ConfiguracionDashboardDto guardarPreferencias(String idAdmin, List<WidgetDto> widgetsDto) {
        ConfiguracionDashboard config = configuracionGateway.buscarPorAdminId(idAdmin)
                .orElse(new ConfiguracionDashboard(idAdmin));

        List<Widget> nuevosWidgets = new ArrayList<>();
        for (WidgetDto w : widgetsDto) {
            TipoWidget tipo = TipoWidget.valueOf(w.getTipo().toUpperCase());
            Posicion pos = new Posicion(w.getPosX(), w.getPosY());
            Tamano tam = new Tamano(w.getAncho(), w.getAlto());
            nuevosWidgets.add(new Widget(tipo, pos, tam));
        }

        config.actualizarWidgets(nuevosWidgets);
        configuracionGateway.guardar(config);

        return toDto(config);
    }

    private ConfiguracionDashboardDto toDto(ConfiguracionDashboard config) {
        List<WidgetDto> widgetsDto = config.getWidgets().stream()
                .map(w -> new WidgetDto(
                        w.getTipo().name(),
                        w.getPosicion().x(),
                        w.getPosicion().y(),
                        w.getTamano().ancho(),
                        w.getTamano().alto()
                ))
                .collect(Collectors.toList());
        return new ConfiguracionDashboardDto(config.getIdAdmin(), widgetsDto);
    }
}
