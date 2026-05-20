package com.openlib.market.infrastructure.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.application.dashboard.ConfiguracionDashboardDto;
import com.openlib.market.application.dashboard.WidgetDto;
import com.openlib.market.domain.dashboard.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConfiguracionAdminJsonGateway implements IConfiguracionAdminGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ConfiguracionDashboardDto> baseDatosEnMemoria;

    public ConfiguracionAdminJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("config_dashboards.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ConfiguracionDashboardDto>>() {});
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.baseDatosEnMemoria = new ArrayList<>();
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ConfiguracionDashboard> buscarPorAdminId(String idAdmin) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.getIdAdmin().equals(idAdmin))
                .findFirst()
                .map(this::mapToDomain);
    }

    @Override
    public void guardar(ConfiguracionDashboard config) {
        baseDatosEnMemoria.removeIf(dto -> dto.getIdAdmin().equals(config.getIdAdmin()));
        
        List<WidgetDto> widgetsDto = config.getWidgets().stream()
                .map(w -> new WidgetDto(
                        w.getTipo().name(),
                        w.getPosicion().x(),
                        w.getPosicion().y(),
                        w.getTamano().ancho(),
                        w.getTamano().alto()
                ))
                .collect(Collectors.toList());

        baseDatosEnMemoria.add(new ConfiguracionDashboardDto(config.getIdAdmin(), widgetsDto));
        guardarDatos();
    }

    private ConfiguracionDashboard mapToDomain(ConfiguracionDashboardDto dto) {
        List<Widget> widgets = new ArrayList<>();
        if (dto.getWidgets() != null) {
            for (WidgetDto w : dto.getWidgets()) {
                widgets.add(new Widget(
                        TipoWidget.valueOf(w.getTipo()),
                        new Posicion(w.getPosX(), w.getPosY()),
                        new Tamano(w.getAncho(), w.getAlto())
                ));
            }
        }
        return new ConfiguracionDashboard(dto.getIdAdmin(), widgets);
    }
}
