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
        this.baseDatosEnMemoria.add(new AdminDto("admin-1", "admin@openlib.com", "admin123", "ROLE_ADMIN", new ArrayList<>()));
    }

    @Override
    public Optional<Administrador> buscarPorEmail(Email email) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.email().equalsIgnoreCase(email.getDireccion()))
                .filter(dto -> "ROLE_ADMIN".equals(dto.rol()))
                .findFirst()
                .map(this::mapToDomain);
    }

    @Override
    public Optional<Administrador> buscarPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .findFirst()
                .map(this::mapToDomain);
    }

    @Override
    public void guardar(Administrador admin) {
        baseDatosEnMemoria.removeIf(dto -> dto.id().equals(admin.getId()));
        baseDatosEnMemoria.add(new AdminDto(
                admin.getId(),
                admin.getEmail().getDireccion(),
                admin.getHashContrasena(),
                admin.getRol().name(),
                admin.getRoles().stream()
                        .map(r -> new RolAdminDto(r.getNombre().name(), r.getPermisos()))
                        .toList()
        ));
        guardarDatos();
    }

    @Override
    public int contarSuperAdmins() {
        return (int) baseDatosEnMemoria.stream()
                .filter(dto -> dto.roles() != null && dto.roles().stream().anyMatch(r -> "SUPERADMIN".equals(r.nombre())))
                .count();
    }

    private Administrador mapToDomain(AdminDto dto) {
        List<RolAdmin> roles = new ArrayList<>();
        if (dto.roles() != null) {
            for (RolAdminDto rDto : dto.roles()) {
                roles.add(new RolAdmin(NombreRolAdmin.valueOf(rDto.nombre()), rDto.permisos()));
            }
        }
        return new Administrador(
                dto.id(),
                new Email(dto.email()),
                dto.hashContrasena(),
                Rol.valueOf(dto.rol()),
                roles
        );
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private record AdminDto(String id, String email, String hashContrasena, String rol, List<RolAdminDto> roles) {}
    private record RolAdminDto(String nombre, List<String> permisos) {}
}
