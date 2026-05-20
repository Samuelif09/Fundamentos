package com.openlib.market.infrastructure.soporte;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.soporte.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketSoporteJsonGateway implements ITicketSoporteGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<TicketDto> baseDatosEnMemoria;

    public TicketSoporteJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("tickets.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<TicketDto>>() {});
            } catch (Exception e) {
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
    }

    @Override
    public List<TicketSoporte> listarPorEstados(List<EstadoTicket> estados, int page, int size) {
        List<String> estadosStr = estados.stream().map(Enum::name).toList();
        return baseDatosEnMemoria.stream()
                .filter(dto -> estadosStr.contains(dto.estado()))
                .skip((long) page * size)
                .limit(size)
                .map(dto -> new TicketSoporte(
                        dto.id(),
                        dto.idUsuario(),
                        dto.asunto(),
                        dto.descripcion(),
                        EstadoTicket.valueOf(dto.estado()),
                        Prioridad.valueOf(dto.prioridad()),
                        dto.fechaCreacion() != null ? LocalDateTime.parse(dto.fechaCreacion()) : LocalDateTime.now()
                ))
                .toList();
    }

    private record TicketDto(String id, String idUsuario, String asunto, String descripcion, String estado, String prioridad, String fechaCreacion) {}
}
