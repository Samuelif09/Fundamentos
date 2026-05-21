package com.openlib.market.infrastructure.configuracion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.application.configuracion.ComisionDto;
import com.openlib.market.domain.configuracion.IConfiguracionComisionGateway;
import com.openlib.market.domain.configuracion.ReglaComision;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfiguracionComisionJsonGateway implements IConfiguracionComisionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ComisionDto> baseDatosEnMemoria;

    public ConfiguracionComisionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("comisiones.json");
        cargarDatos();
        
        // Si no hay datos, crear la regla GLOBAL por defecto al 10%
        if (baseDatosEnMemoria.isEmpty()) {
            guardarRegla(new ReglaComision("GLOBAL", 10.0));
        }
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ComisionDto>>() {});
            } catch (Exception e) {
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReglaComision obtenerRegla(String idCategoria) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.getIdCategoria().equalsIgnoreCase(idCategoria))
                .findFirst()
                .map(dto -> new ReglaComision(dto.getIdCategoria(), dto.getPorcentajeComision()))
                .orElse(null);
    }

    @Override
    public void guardarRegla(ReglaComision regla) {
        // Remover si ya existe para la misma categoría
        baseDatosEnMemoria.removeIf(dto -> dto.getIdCategoria().equalsIgnoreCase(regla.getIdCategoria()));
        
        baseDatosEnMemoria.add(new ComisionDto(regla.getIdCategoria(), regla.getPorcentajeComision()));
        guardarDatos();
    }

    @Override
    public List<ReglaComision> listarTodas() {
        return baseDatosEnMemoria.stream()
                .map(dto -> new ReglaComision(dto.getIdCategoria(), dto.getPorcentajeComision()))
                .toList();
    }
}
