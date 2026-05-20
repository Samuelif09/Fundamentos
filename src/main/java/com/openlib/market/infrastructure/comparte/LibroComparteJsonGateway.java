package com.openlib.market.infrastructure.comparte;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.comparte.ILibroComparteGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class LibroComparteJsonGateway implements ILibroComparteGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroDto> baseDatosEnMemoria;

    public LibroComparteJsonGateway() {
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
            }
        }
        
        if (this.baseDatosEnMemoria == null || this.baseDatosEnMemoria.isEmpty()) {
            this.baseDatosEnMemoria = List.of(
                new LibroDto("978-3-16-148410-0", "Clean Code", "Una guía de código limpio.", 29.99, "http://portada.com/clean-code.jpg")
            );
        }
    }

    @Override
    public boolean existeLibroActivo(String isbn) {
        return baseDatosEnMemoria.stream()
                .anyMatch(dto -> dto.isbn().equals(isbn));
    }

    private record LibroDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada) {}
}
