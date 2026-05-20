package com.openlib.market.infrastructure.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.dashboard.IDashboardLibroGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Implementación de IDashboardLibroGateway.
 * Entrega 1: cuenta libros desde libros.json para obtener el KPI de "libros pendientes" (AC-001 A-02).
 */
@Component
public class DashboardLibroJsonGateway implements IDashboardLibroGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;

    public DashboardLibroJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("libros.json");
    }

    private List<Map<String, Object>> cargarLibros() {
        if (jsonFile.exists()) {
            try {
                return objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Datos simulados para entorno de desarrollo
        return List.of(
                Map.of("id", "l-1", "estado", "PENDIENTE_APROBACION"),
                Map.of("id", "l-2", "estado", "APROBADO"),
                Map.of("id", "l-3", "estado", "PENDIENTE_APROBACION")
        );
    }

    @Override
    public long contarLibrosPendientesAprobacion() {
        return cargarLibros().stream()
                .filter(l -> "PENDIENTE_APROBACION".equals(l.get("estado")))
                .count();
    }

    @Override
    public long contarTotalLibros() {
        return cargarLibros().size();
    }
}
