package com.openlib.market.infrastructure.autenticacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.IUsuarioAuthGateway;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UsuarioAuthJsonGateway implements IUsuarioAuthGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<UsuarioDto> baseDatosEnMemoria;

    public UsuarioAuthJsonGateway() {
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
        
        // Agregar un usuario por defecto si está vacío para pruebas de C-01
        if (this.baseDatosEnMemoria.isEmpty()) {
            // El hash será dummy en texto plano para el MVP si no se usa Spring Security aún
            this.baseDatosEnMemoria.add(new UsuarioDto("1", "comprador@openlib.com", "123456"));
        }
    }

    @Override
    public Optional<UsuarioAuth> buscarPorEmail(Email email) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.email().equalsIgnoreCase(email.getDireccion()))
                .findFirst()
                .map(dto -> new UsuarioAuth(dto.id(), new Email(dto.email()), dto.hashContrasena()));
    }

    private record UsuarioDto(String id, String email, String hashContrasena) {}
}
