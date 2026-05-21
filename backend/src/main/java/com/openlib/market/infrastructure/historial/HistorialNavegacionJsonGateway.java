package com.openlib.market.infrastructure.historial;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.historial.HistorialNavegacion;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.historial.ItemNavegacion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HistorialNavegacionJsonGateway implements IHistorialNavegacionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private Map<String, HistorialDto> baseDatosEnMemoria;

    public HistorialNavegacionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("historial_navegacion.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<Map<String, HistorialDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new HashMap<>();
            }
        } else {
            this.baseDatosEnMemoria = new HashMap<>();
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
    public Optional<HistorialNavegacion> obtenerPorUsuario(String idUsuario) {
        HistorialDto dto = baseDatosEnMemoria.get(idUsuario);
        if (dto == null) {
            return Optional.empty();
        }
        
        List<ItemNavegacion> items = dto.items().stream()
                .map(itemDto -> new ItemNavegacion(itemDto.idLibro(), java.time.LocalDateTime.parse(itemDto.fechaVista())))
                .collect(Collectors.toList());

        return Optional.of(new HistorialNavegacion(idUsuario, items));
    }

    @Override
    public void guardar(HistorialNavegacion historial) {
        List<ItemNavegacionDto> itemsDto = historial.getItems().stream()
                .map(item -> new ItemNavegacionDto(item.getIdLibro(), item.getFechaVista().toString()))
                .collect(Collectors.toList());

        baseDatosEnMemoria.put(historial.getIdUsuario(), new HistorialDto(historial.getIdUsuario(), itemsDto));
        guardarDatos();
    }

    private record ItemNavegacionDto(String idLibro, String fechaVista) {}
    private record HistorialDto(String idUsuario, List<ItemNavegacionDto> items) {}
}
