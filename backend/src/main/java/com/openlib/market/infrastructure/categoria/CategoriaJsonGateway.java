package com.openlib.market.infrastructure.categoria;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.categoria.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CategoriaJsonGateway implements ICategoriaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<CategoriaDto> baseDatosEnMemoria;

    public CategoriaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("categorias.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<CategoriaDto>>() {});
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
    public void guardar(CategoriaCatalogo categoria) {
        baseDatosEnMemoria.add(new CategoriaDto(categoria.getId(), categoria.getNombre().getValor(), categoria.getEstado().name()));
        guardarDatos();
    }

    @Override
    public void actualizar(CategoriaCatalogo categoria) {
        baseDatosEnMemoria.removeIf(c -> c.id().equals(categoria.getId()));
        guardar(categoria);
    }

    @Override
    public Optional<CategoriaCatalogo> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .map(c -> new CategoriaCatalogo(c.id(), new NombreCategoria(c.nombre()), EstadoCategoria.valueOf(c.estado())));
    }

    @Override
    public boolean existePorNombreNormalizado(String nombreNormalizado) {
        return baseDatosEnMemoria.stream()
                .anyMatch(c -> c.nombre().toLowerCase().equals(nombreNormalizado));
    }

    @Override
    public List<CategoriaCatalogo> listarTodas() {
        return baseDatosEnMemoria.stream()
                .map(c -> new CategoriaCatalogo(c.id(), new NombreCategoria(c.nombre()), EstadoCategoria.valueOf(c.estado())))
                .toList();
    }

    private record CategoriaDto(String id, String nombre, String estado) {}
}
