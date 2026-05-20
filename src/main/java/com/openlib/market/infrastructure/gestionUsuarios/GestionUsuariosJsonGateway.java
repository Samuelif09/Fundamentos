package com.openlib.market.infrastructure.gestionUsuarios;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.EstadoCuenta;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.MotivoSuspension;
import com.openlib.market.domain.registro.Password;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("gestionUsuariosJsonGateway")
public class GestionUsuariosJsonGateway implements IUsuarioGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<UsuarioDto> baseDatosEnMemoria;

    public GestionUsuariosJsonGateway() {
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
    public Optional<Usuario> buscarPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(u -> u.id().equals(id))
                .findFirst()
                .map(dto -> {
                    EstadoCuenta estado = dto.estadoCuenta() != null ? EstadoCuenta.valueOf(dto.estadoCuenta()) : EstadoCuenta.ACTIVO;
                    MotivoSuspension motivo = dto.motivoSuspension() != null ? new MotivoSuspension(dto.motivoSuspension()) : null;
                    return new Usuario(
                        dto.id(),
                        dto.nombre(),
                        new Email(dto.email()),
                        new Password("dummyPass1"), // Contraseña simulada porque no viene en el dto o viene hasheada
                        RolUsuario.valueOf(dto.rol()),
                        estado,
                        motivo
                    );
                });
    }

    @Override
    public void actualizar(Usuario usuario) {
        // Buscamos el DTO a actualizar
        for (int i = 0; i < baseDatosEnMemoria.size(); i++) {
            if (baseDatosEnMemoria.get(i).id().equals(usuario.getId())) {
                UsuarioDto viejo = baseDatosEnMemoria.get(i);
                UsuarioDto actualizado = new UsuarioDto(
                        viejo.id(),
                        viejo.nombre(),
                        viejo.email(),
                        viejo.password(),
                        viejo.rol(),
                        usuario.getEstadoCuenta().name(),
                        usuario.getMotivoSuspension() != null ? usuario.getMotivoSuspension().getRazon() : null
                );
                baseDatosEnMemoria.set(i, actualizado);
                guardarDatos();
                return;
            }
        }
    }

    private record UsuarioDto(String id, String nombre, String email, String password, String rol, String estadoCuenta, String motivoSuspension) {}

    @Override
    public List<Usuario> listarTodos() {
        return baseDatosEnMemoria.stream()
                .map(dto -> {
                    EstadoCuenta estado = dto.estadoCuenta() != null ? EstadoCuenta.valueOf(dto.estadoCuenta()) : EstadoCuenta.ACTIVO;
                    MotivoSuspension motivo = dto.motivoSuspension() != null ? new MotivoSuspension(dto.motivoSuspension()) : null;
                    return new Usuario(
                        dto.id(),
                        dto.nombre(),
                        new Email(dto.email()),
                        new Password("dummyPass1"),
                        RolUsuario.valueOf(dto.rol()),
                        estado,
                        motivo
                    );
                })
                .toList();
    }
}
