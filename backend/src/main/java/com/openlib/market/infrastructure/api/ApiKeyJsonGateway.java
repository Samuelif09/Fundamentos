package com.openlib.market.infrastructure.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.application.api.CredencialApiDto;
import com.openlib.market.domain.api.CredencialApi;
import com.openlib.market.domain.api.EstadoLlave;
import com.openlib.market.domain.api.IApiKeyGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ApiKeyJsonGateway implements IApiKeyGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<CredencialApiDto> baseDatosEnMemoria;

    public ApiKeyJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("api_keys.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<CredencialApiDto>>() {});
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
    public void guardar(CredencialApi credencial) {
        baseDatosEnMemoria.removeIf(dto -> dto.getId().equals(credencial.getId()));
        baseDatosEnMemoria.add(new CredencialApiDto(
                credencial.getId(),
                credencial.getIdPropietario(),
                credencial.getNombreApp(),
                credencial.getLlave().valor(),
                credencial.getEstado().name()
        ));
        guardarDatos();
    }

    @Override
    public Optional<CredencialApi> buscarPorLlave(String valorLlave) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.getLlave().equals(valorLlave))
                .findFirst()
                .map(this::mapToDomain);
    }

    @Override
    public Optional<CredencialApi> buscarPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst()
                .map(this::mapToDomain);
    }

    private CredencialApi mapToDomain(CredencialApiDto dto) {
        return new CredencialApi(
                dto.getId(),
                dto.getIdPropietario(),
                dto.getNombreApp(),
                dto.getLlave(),
                EstadoLlave.valueOf(dto.getEstado())
        );
    }
}
