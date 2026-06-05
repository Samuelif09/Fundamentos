package com.openlib.market.infrastructure.vendedor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.IdentificacionTributaria;
import com.openlib.market.domain.vendedor.RazonSocial;
import com.openlib.market.domain.vendedor.Vendedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.openlib.market.domain.vendedor.EstadoVerificacion;

@Component
@Profile("mock")
public class VendedorJsonGateway implements IVendedorGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendedorJsonGateway.class);

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<VendedorDto> baseDatosEnMemoria;

    public VendedorJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("vendedores.json");
        LOGGER.info("[PERSISTENCIA_JSON] VendedorJsonGateway inicializado. Archivo={}", jsonFile.getAbsolutePath());
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<VendedorDto>>() {});
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
    public void guardar(Vendedor vendedor) {
        LOGGER.info("[PERSISTENCIA_JSON] Guardando vendedor en archivo {}. id={}, idUsuario={}, nit={}",
            jsonFile.getAbsolutePath(), vendedor.getId(), vendedor.getIdUsuario(), vendedor.getIdentificacionTributaria().getValor());
        VendedorDto dto = new VendedorDto(
                vendedor.getId(),
                vendedor.getIdUsuario(),
                vendedor.getRazonSocial().getValor(),
                vendedor.getIdentificacionTributaria().getValor(),
                vendedor.getEstadoVerificacion().name()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
        LOGGER.info("[PERSISTENCIA_JSON] Vendedor persistido en archivo {}. id={}", jsonFile.getAbsolutePath(), vendedor.getId());
    }

    @Override
    public void actualizar(Vendedor vendedor) {
        baseDatosEnMemoria.removeIf(dto -> dto.id().equals(vendedor.getId()));
        guardar(vendedor);
    }

    @Override
    public Optional<Vendedor> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .map(dto -> new Vendedor(
                        dto.id(),
                        dto.idUsuario(),
                        new RazonSocial(dto.razonSocial()),
                        new IdentificacionTributaria(dto.identificacionTributaria()),
                        dto.estadoVerificacion() != null ? EstadoVerificacion.valueOf(dto.estadoVerificacion()) : EstadoVerificacion.NO_INICIADO
                ))
                .findFirst();
    }

    @Override
    public boolean existePorIdentificacionTributaria(String identificacionTributaria) {
        return baseDatosEnMemoria.stream()
                .anyMatch(v -> v.identificacionTributaria().equals(identificacionTributaria));
    }

    private record VendedorDto(String id, String idUsuario, String razonSocial, String identificacionTributaria, String estadoVerificacion) {}
}
