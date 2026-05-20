package com.openlib.market.infrastructure.reembolso;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.reembolso.EstadoReembolso;
import com.openlib.market.domain.reembolso.IReembolsoGateway;
import com.openlib.market.domain.reembolso.SolicitudReembolso;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReembolsoJsonGateway implements IReembolsoGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ReembolsoDto> baseDatosEnMemoria;

    public ReembolsoJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("reembolsos.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ReembolsoDto>>() {});
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
    public void guardar(SolicitudReembolso solicitud) {
        baseDatosEnMemoria.add(new ReembolsoDto(
                solicitud.getId(),
                solicitud.getIdPedido(),
                solicitud.getMontoReembolso(),
                solicitud.getMotivo(),
                solicitud.getEstado().name()
        ));
        guardarDatos();
    }

    @Override
    public void actualizar(SolicitudReembolso solicitud) {
        baseDatosEnMemoria.removeIf(r -> r.id().equals(solicitud.getId()));
        guardar(solicitud);
    }

    @Override
    public Optional<SolicitudReembolso> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(r -> r.id().equals(id))
                .findFirst()
                .map(r -> new SolicitudReembolso(
                        r.id(),
                        r.idPedido(),
                        r.montoReembolso(),
                        r.motivo(),
                        EstadoReembolso.valueOf(r.estado())
                ));
    }

    @Override
    public List<SolicitudReembolso> listarTodas() {
        return baseDatosEnMemoria.stream()
                .map(r -> new SolicitudReembolso(
                        r.id(),
                        r.idPedido(),
                        r.montoReembolso(),
                        r.motivo(),
                        EstadoReembolso.valueOf(r.estado())
                )).toList();
    }

    private record ReembolsoDto(String id, String idPedido, double montoReembolso, String motivo, String estado) {}
}
