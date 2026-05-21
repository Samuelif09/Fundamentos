package com.openlib.market.infrastructure.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.dashboard.IDashboardPedidoGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Implementación de IDashboardPedidoGateway.
 * Entrega 1: calcula ingresos y conteo de pedidos desde pedidos.json.
 * Entrega 2: reemplazar con consulta a tabla materializada en PostgreSQL.
 */
@Component
public class DashboardPedidoJsonGateway implements IDashboardPedidoGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;

    public DashboardPedidoJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("pedidos.json");
    }

    private List<Map<String, Object>> cargarPedidos() {
        if (jsonFile.exists()) {
            try {
                return objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Datos simulados para entorno de desarrollo/prueba
        return List.of(
                Map.of("id", "p-1", "total", 49.99, "fecha", java.time.LocalDate.now().toString()),
                Map.of("id", "p-2", "total", 19.99, "fecha", java.time.LocalDate.now().toString()),
                Map.of("id", "p-3", "total", 129.50, "fecha", "2026-05-15")
        );
    }

    @Override
    public long contarPedidosHoy() {
        String hoy = java.time.LocalDate.now().toString();
        return cargarPedidos().stream()
                .filter(p -> hoy.equals(p.get("fecha")))
                .count();
    }

    @Override
    public double calcularIngresosHoy() {
        String hoy = java.time.LocalDate.now().toString();
        return cargarPedidos().stream()
                .filter(p -> hoy.equals(p.get("fecha")))
                .mapToDouble(p -> ((Number) p.get("total")).doubleValue())
                .sum();
    }

    @Override
    public double calcularIngresosTotales() {
        return cargarPedidos().stream()
                .mapToDouble(p -> ((Number) p.get("total")).doubleValue())
                .sum();
    }
}
