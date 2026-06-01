package com.openlib.market.infrastructure.inventario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.detalle.IActualizarLibroGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ActualizarLibroJsonGateway implements IActualizarLibroGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroEditableDto> baseDatosEnMemoria;

    public ActualizarLibroJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("libros_publicados.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LibroEditableDto>>() {});
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
    public Optional<Libro> buscarPorIsbn(Isbn isbn) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.isbn().equals(isbn.getValor()))
                .map(dto -> new Libro(
                        new Isbn(dto.isbn()),
                        dto.titulo(),
                        dto.sinopsis(),
                        new Precio(dto.precio()),
                        dto.urlPortada(),
                        dto.categoria(),
                        dto.idVendedor()
                ))
                .findFirst();
    }

    @Override
    public void actualizar(Libro libro) {
        baseDatosEnMemoria.replaceAll(dto -> {
            if (dto.isbn().equals(libro.getIsbn().getValor())) {
                return new LibroEditableDto(
                        dto.isbn(), dto.titulo(), dto.sinopsis(),
                        libro.getPrecio().getValor(),
                        dto.urlPortada(), dto.categoria(), dto.idVendedor()
                );
            }
            return dto;
        });
        guardarDatos();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LibroEditableDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor) {}
}
