package com.openlib.market.infrastructure.suscripcion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.suscripcion.ISuscripcionGateway;
import com.openlib.market.domain.suscripcion.SuscripcionAutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class SuscripcionJsonGateway implements ISuscripcionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<SuscripcionDto> baseDatosEnMemoria;

    public SuscripcionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("suscripciones.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<SuscripcionDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
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
    public void guardar(SuscripcionAutor suscripcion) {
        SuscripcionDto dto = new SuscripcionDto(
                suscripcion.getIdComprador(),
                suscripcion.getIdVendedor(),
                suscripcion.getFechaSuscripcion().toString()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public boolean existeSuscripcion(String idComprador, String idVendedor) {
        return baseDatosEnMemoria.stream()
                .anyMatch(s -> s.idComprador().equals(idComprador) && s.idVendedor().equals(idVendedor));
    }

    private record SuscripcionDto(String idComprador, String idVendedor, String fecha) {}
}
