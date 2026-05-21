package com.openlib.market.infrastructure.registro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.registro.Usuario;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class RegistroJsonGateway implements IRegistroGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<UsuarioDto> baseDatosEnMemoria;

    public RegistroJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("usuarios.json");
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
        UsuarioDto dto = new UsuarioDto(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail().getValor(),
            usuario.getPassword().getValor(),
            usuario.getRol().name()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public boolean existeEmail(Email email) {
        return baseDatosEnMemoria.stream()
                .anyMatch(u -> u.email().equalsIgnoreCase(email.getValor()));
    }

    private record UsuarioDto(String id, String nombre, String email, String password, String rol) {}
}
