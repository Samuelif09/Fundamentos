package com.openlib.market.infrastructure.detalle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
public class DetalleJsonGateway implements IDetalleGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroDto> baseDatosEnMemoria;

    public DetalleJsonGateway() {
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
    public Optional<Libro> buscarPorId(Isbn isbn) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.isbn().equals(isbn.getValor()))
                .map(dto -> new Libro(
                        new Isbn(dto.isbn()),
                        dto.titulo(),
                        dto.sinopsis(),
                        new Precio(dto.precio()),
                        dto.urlPortada()
                ))
                .findFirst();
    }

    private record LibroDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada) {}
}
