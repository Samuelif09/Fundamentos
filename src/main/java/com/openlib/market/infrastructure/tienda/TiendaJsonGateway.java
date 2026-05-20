package com.openlib.market.infrastructure.tienda;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;
import com.openlib.market.domain.tienda.UrlAmigable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TiendaJsonGateway implements ITiendaVendedorGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<VendedorDto> baseDatosEnMemoria;

    public TiendaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("vendedores.json");
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

    @Override
    public Optional<PerfilTiendaBase> obtenerPerfilPorSlug(String slug) {
        return baseDatosEnMemoria.stream()
                .filter(v -> {
                    String slugGenerado = new UrlAmigable(v.razonSocial()).getValor();
                    return slugGenerado.equals(slug);
                })
                .map(v -> new PerfilTiendaBase(v.id(), v.razonSocial(), slug, v.urlBanner()))
                .findFirst();
    }

    @Override
    public Optional<PerfilTiendaBase> obtenerPerfilPorIdVendedor(String idVendedor) {
        return baseDatosEnMemoria.stream()
                .filter(v -> idVendedor.equals(v.id()))
                .map(v -> {
                    String slug = new UrlAmigable(v.razonSocial()).getValor();
                    return new PerfilTiendaBase(v.id(), v.razonSocial(), slug, v.urlBanner());
                })
                .findFirst();
    }

    @Override
    public void actualizarBanner(String idVendedor, String urlBanner) {
        for (int i = 0; i < baseDatosEnMemoria.size(); i++) {
            VendedorDto dto = baseDatosEnMemoria.get(i);
            if (idVendedor.equals(dto.id())) {
                baseDatosEnMemoria.set(i, new VendedorDto(dto.id(), dto.idUsuario(), dto.razonSocial(), dto.identificacionTributaria(), urlBanner));
                break;
            }
        }
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private record VendedorDto(String id, String idUsuario, String razonSocial, String identificacionTributaria, String urlBanner) {}
}
