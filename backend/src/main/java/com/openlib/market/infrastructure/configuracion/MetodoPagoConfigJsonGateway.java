package com.openlib.market.infrastructure.configuracion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.configuracion.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MetodoPagoConfigJsonGateway implements IMetodoPagoConfigGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ConfiguracionDto> baseDatosEnMemoria;

    public MetodoPagoConfigJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("metodos_pago.json");
        cargarDatos();
        if (baseDatosEnMemoria.isEmpty()) {
            // Seed inicial
            baseDatosEnMemoria.add(new ConfiguracionDto("stripe", "Stripe", "HABILITADO"));
            baseDatosEnMemoria.add(new ConfiguracionDto("paypal", "PayPal", "HABILITADO"));
            baseDatosEnMemoria.add(new ConfiguracionDto("transferencia", "Transferencia Bancaria", "DESHABILITADO"));
            guardarDatos();
        }
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ConfiguracionDto>>() {});
            } catch (Exception e) {
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
    public void actualizar(ConfiguracionMetodoPago configuracion) {
        baseDatosEnMemoria.removeIf(c -> c.id().equals(configuracion.getId()));
        baseDatosEnMemoria.add(new ConfiguracionDto(configuracion.getId(), configuracion.getNombre().getValor(), configuracion.getEstado().name()));
        guardarDatos();
    }

    @Override
    public Optional<ConfiguracionMetodoPago> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .map(c -> new ConfiguracionMetodoPago(c.id(), new NombreMetodo(c.nombre()), EstadoMetodoPago.valueOf(c.estado())));
    }

    @Override
    public List<ConfiguracionMetodoPago> listarTodos() {
        return baseDatosEnMemoria.stream()
                .map(c -> new ConfiguracionMetodoPago(c.id(), new NombreMetodo(c.nombre()), EstadoMetodoPago.valueOf(c.estado())))
                .toList();
    }

    @Override
    public int contarMetodosHabilitados() {
        return (int) baseDatosEnMemoria.stream()
                .filter(c -> "HABILITADO".equals(c.estado()))
                .count();
    }

    private record ConfiguracionDto(String id, String nombre, String estado) {}
}
