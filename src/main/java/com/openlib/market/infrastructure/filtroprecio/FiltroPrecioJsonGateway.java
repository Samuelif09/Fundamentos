package com.openlib.market.infrastructure.filtroprecio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.filtroprecio.IFiltroPrecioGateway;
import com.openlib.market.domain.filtroprecio.LibroFiltro;
import com.openlib.market.domain.filtroprecio.RangoPrecio;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FiltroPrecioJsonGateway implements IFiltroPrecioGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroDto> baseDatosEnMemoria;

    public FiltroPrecioJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("libros.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LibroDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
        
        if (this.baseDatosEnMemoria.isEmpty()) {
            this.baseDatosEnMemoria.add(new LibroDto("978-1", "Libro Barato", "Sinopsis", 15.0, "url"));
            this.baseDatosEnMemoria.add(new LibroDto("978-2", "Libro Medio", "Sinopsis", 30.0, "url"));
            this.baseDatosEnMemoria.add(new LibroDto("978-3", "Libro Caro", "Sinopsis", 80.0, "url"));
        }
    }

    @Override
    public List<LibroFiltro> buscarPorRango(RangoPrecio rango) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> rango.estaDentroDelRango(dto.precio()))
                .map(dto -> new LibroFiltro(dto.isbn(), dto.titulo(), dto.precio()))
                .collect(Collectors.toList());
    }

    private record LibroDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada) {}
}
