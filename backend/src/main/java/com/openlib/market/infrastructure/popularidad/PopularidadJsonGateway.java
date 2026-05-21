package com.openlib.market.infrastructure.popularidad;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.popularidad.IPopularidadGateway;
import com.openlib.market.domain.popularidad.LibroPopularidad;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopularidadJsonGateway implements IPopularidadGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroPopularDto> baseDatosEnMemoria;

    public PopularidadJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("popularidad.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LibroPopularDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
        
        if (this.baseDatosEnMemoria.isEmpty()) {
            this.baseDatosEnMemoria.add(new LibroPopularDto("978-1", "Libro A", 120));
            this.baseDatosEnMemoria.add(new LibroPopularDto("978-2", "Libro B", 35));
            this.baseDatosEnMemoria.add(new LibroPopularDto("978-3", "Libro C", 400));
            this.baseDatosEnMemoria.add(new LibroPopularDto("978-4", "Libro D", 85));
        }
    }

    @Override
    public List<LibroPopularidad> obtenerTodos() {
        return baseDatosEnMemoria.stream()
                .map(dto -> new LibroPopularidad(dto.isbn(), dto.titulo(), dto.ventasUltimoMes()))
                .collect(Collectors.toList());
    }

    private record LibroPopularDto(String isbn, String titulo, int ventasUltimoMes) {}
}
