package com.openlib.market.infrastructure.soporte;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.soporte.Disputa;
import com.openlib.market.domain.soporte.EstadoDisputa;
import com.openlib.market.domain.soporte.IDisputaGateway;
import com.openlib.market.domain.soporte.Resolucion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DisputaJsonGateway implements IDisputaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<DisputaDto> baseDatosEnMemoria;

    public DisputaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("disputas.json");
        cargarDatos();
        
        // Mock data
        if (baseDatosEnMemoria.isEmpty()) {
            Disputa disputaMock = new Disputa("ped-123", "comp-123", "vend-123", "El libro llegó roto");
            guardar(disputaMock);
        }
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<DisputaDto>>() {});
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
    public Optional<Disputa> buscarPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .findFirst()
                .map(dto -> new Disputa(
                        dto.id(),
                        dto.idPedido(),
                        dto.idComprador(),
                        dto.idVendedor(),
                        dto.motivo(),
                        EstadoDisputa.valueOf(dto.estado()),
                        Resolucion.valueOf(dto.resolucion())
                ));
    }

    @Override
    public void guardar(Disputa disputa) {
        baseDatosEnMemoria.removeIf(dto -> dto.id().equals(disputa.getId()));
        baseDatosEnMemoria.add(new DisputaDto(
                disputa.getId(),
                disputa.getIdPedido(),
                disputa.getIdComprador(),
                disputa.getIdVendedor(),
                disputa.getMotivo(),
                disputa.getEstado().name(),
                disputa.getResolucion().name()
        ));
        guardarDatos();
    }

    private record DisputaDto(String id, String idPedido, String idComprador, String idVendedor, String motivo, String estado, String resolucion) {}
}
