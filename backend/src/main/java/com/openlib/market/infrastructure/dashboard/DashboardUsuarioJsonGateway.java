package com.openlib.market.infrastructure.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.dashboard.IDashboardUsuarioGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementación de IDashboardUsuarioGateway.
 * Entrega 1: cuenta usuarios cargados desde usuarios.json.
 * Entrega 2: reemplazar con consulta a Redis/PostgreSQL Materialized View.
 */
@Component
public class DashboardUsuarioJsonGateway implements IDashboardUsuarioGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;

    public DashboardUsuarioJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("usuarios.json");
    }

    private List<Map<String, Object>> cargarUsuarios() {
        if (jsonFile.exists()) {
            try {
                return objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Datos simulados para entorno de desarrollo
        return List.of(
                Map.of("id", "1", "email", "comprador@openlib.com", "fechaRegistro", "2026-05-19"),
                Map.of("id", "2", "email", "otro@openlib.com", "fechaRegistro", "2026-05-18")
        );
    }

    @Override
    public long contarNuevosUsuariosHoy() {
        String hoy = java.time.LocalDate.now().toString();
        return cargarUsuarios().stream()
                .filter(u -> hoy.equals(u.get("fechaRegistro")))
                .count();
    }

    @Override
    public long contarTotalUsuarios() {
        return cargarUsuarios().size();
    }
}
