package com.openlib.market.infrastructure.publicacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.openlib.market.domain.curaduria.ICuraduriaGateway;

@Component
@Profile("mock")
public class LibroPublicacionJsonGateway implements ILibroPublicacionGateway, ICuraduriaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroDto> baseDatosEnMemoria;

    public LibroPublicacionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("libros_publicados.json");
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
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(Libro libro) {
        LibroDto dto = new LibroDto(
                libro.getIsbn().getValor(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getPrecio().getValor(),
                libro.getUrlPortada(),
                libro.getCategoria(),
                libro.getIdVendedor(),
                libro.getEstado(),
                libro.getUrlVistaPrevia()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public java.util.Optional<Libro> obtenerPorIsbn(String isbn) {
        return baseDatosEnMemoria.stream()
                .filter(l -> l.isbn().equals(isbn))
                .findFirst()
                .map(dto -> new Libro(
                        new com.openlib.market.domain.detalle.Isbn(dto.isbn()),
                        dto.titulo(),
                        dto.sinopsis(),
                        new com.openlib.market.domain.detalle.Precio(dto.precio()),
                        dto.urlPortada(),
                        dto.categoria(),
                        dto.idVendedor(),
                        dto.estado() != null ? dto.estado() : com.openlib.market.domain.detalle.EstadoLibro.ACTIVO,
                        dto.urlVistaPrevia()
                ));
    }

    @Override
    public void actualizar(Libro libro) {
        baseDatosEnMemoria.removeIf(l -> l.isbn().equals(libro.getIsbn().getValor()));
        guardar(libro);
    }

    @Override
    public List<Libro> listarPorEstado(com.openlib.market.domain.detalle.EstadoLibro estado, int page, int size) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.estado() != null && dto.estado() == estado)
                .skip((long) page * size)
                .limit(size)
                .map(dto -> new Libro(
                        new com.openlib.market.domain.detalle.Isbn(dto.isbn()),
                        dto.titulo(),
                        dto.sinopsis(),
                        new com.openlib.market.domain.detalle.Precio(dto.precio()),
                        dto.urlPortada(),
                        dto.categoria(),
                        dto.idVendedor(),
                        dto.estado(),
                        dto.urlVistaPrevia()
                ))
                .toList();
    }

    private record LibroDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor, com.openlib.market.domain.detalle.EstadoLibro estado, String urlVistaPrevia) {}
}
