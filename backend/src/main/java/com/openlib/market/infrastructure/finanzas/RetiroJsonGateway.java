package com.openlib.market.infrastructure.finanzas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.finanzas.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class RetiroJsonGateway implements IRetiroGateway {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile = new File("retiros.json");
    private List<RetiroDto> baseDatosEnMemoria;

    public RetiroJsonGateway() {
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<>() {});
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
    public void guardar(SolicitudRetiro solicitud) {
        RetiroDto dto = new RetiroDto(
                solicitud.getId(),
                solicitud.getIdVendedor(),
                solicitud.getMonto().getValor(),
                solicitud.getCuentaDestino().getValor(),
                solicitud.getEstado().name()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    private record RetiroDto(String id, String idVendedor, double monto, String cuentaDestino, String estado) {}
}
