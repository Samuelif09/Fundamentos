package com.openlib.market.infrastructure.registro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.registro.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("mock")
public class RegistroJsonGateway implements IRegistroGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistroJsonGateway.class);

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<UsuarioDto> baseDatosEnMemoria;

    public RegistroJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("usuarios.json");
        LOGGER.info("[PERSISTENCIA_JSON] RegistroJsonGateway inicializado. Archivo={}", jsonFile.getAbsolutePath());
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<UsuarioDto>>() {});
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
    public void guardar(Usuario usuario) {
        LOGGER.info("[PERSISTENCIA_JSON] Guardando usuario en archivo {}. id={}, email={}",
                jsonFile.getAbsolutePath(), usuario.getId(), usuario.getEmail().getValor());
        UsuarioDto dto = new UsuarioDto(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail().getValor(),
            usuario.getPassword().getValor(),
            usuario.getRol().name()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
        LOGGER.info("[PERSISTENCIA_JSON] Usuario persistido en archivo {}. id={}", jsonFile.getAbsolutePath(), usuario.getId());
    }

    @Override
    public boolean existeEmail(Email email) {
        return baseDatosEnMemoria.stream()
                .anyMatch(u -> u.email().equalsIgnoreCase(email.getValor()));
    }

    private record UsuarioDto(String id, String nombre, String email, String password, String rol) {}
}
