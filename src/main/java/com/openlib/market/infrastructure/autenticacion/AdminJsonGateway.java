package com.openlib.market.infrastructure.autenticacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.autenticacion.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto IAdminGateway.
 * Entrega 1: lee de admins.json o usa un administrador por defecto si el archivo no existe.
 */
@Component
public class AdminJsonGateway implements IAdminGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<AdminDto> baseDatosEnMemoria;

    public AdminJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("admins.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(
                        jsonFile, new TypeReference<List<AdminDto>>() {}
                );
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Administrador por defecto para entornos de desarrollo/prueba
        this.baseDatosEnMemoria = new ArrayList<>();
        this.baseDatosEnMemoria.add(new AdminDto("admin-1", "admin@openlib.com", "admin123", "ROLE_ADMIN"));
    }

    @Override
    public Optional<Administrador> buscarPorEmail(Email email) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.email().equalsIgnoreCase(email.getDireccion()))
                .filter(dto -> "ROLE_ADMIN".equals(dto.rol()))
                .findFirst()
                .map(dto -> new Administrador(
                        dto.id(),
                        new Email(dto.email()),
                        dto.hashContrasena(),
                        Rol.ROLE_ADMIN
                ));
    }

    private record AdminDto(String id, String email, String hashContrasena, String rol) {}
}
