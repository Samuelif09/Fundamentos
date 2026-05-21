package com.openlib.market.infrastructure.catalogo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.PaginaDominio;
import com.openlib.market.domain.catalogo.Paginacion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogoJsonGateway implements ICatalogoGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroDto> baseDatosEnMemoria;

    public CatalogoJsonGateway() {
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

        // Semilla para asegurar que la paginación tenga sentido si está vacío
        if (this.baseDatosEnMemoria.isEmpty()) {
            for (int i = 1; i <= 25; i++) {
                String cat = (i % 2 == 0) ? "FICCIÓN" : "PROGRAMACION";
                this.baseDatosEnMemoria.add(new LibroDto("ISBN-" + i, "Libro Paginado " + i, 15.0, "url", "Autor " + (i%5), cat));
            }
        }
    }

    @Override
    public PaginaDominio<com.openlib.market.domain.catalogo.LibroCatalogo> listarPaginado(Paginacion paginacion) {
        int page = paginacion.getPaginaActual();
        int size = paginacion.getTamanoPagina();
        int totalElements = baseDatosEnMemoria.size();

        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<LibroDto> subList = baseDatosEnMemoria.subList(fromIndex, toIndex);

        List<com.openlib.market.domain.catalogo.LibroCatalogo> domainList = subList.stream()
                .filter(dto -> dto.estado() == null || dto.estado().equals("ACTIVO"))
                .map(dto -> new com.openlib.market.domain.catalogo.LibroCatalogo(dto.isbn(), dto.titulo(), dto.precio(), dto.urlPortada()))
                .toList();

        return new PaginaDominio<>(domainList, page, size, totalElements);
    }

    @Override
    public List<com.openlib.market.domain.catalogo.LibroCatalogo> buscarPorFiltros(com.openlib.market.domain.catalogo.CriterioBusqueda criterio) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> {
                    boolean matchTitulo = true;
                    boolean matchAutor = true;
                    boolean matchCategoria = true;
                    boolean matchPrecio = true;
                    boolean matchEstado = dto.estado() == null || dto.estado().equals("ACTIVO");
                    if (!matchEstado) return false;
                    if (criterio.tieneTitulo()) {
                        matchTitulo = dto.titulo().toLowerCase().contains(criterio.getTitulo().toLowerCase());
                    }
                    if (criterio.tieneAutor()) {
                        matchAutor = dto.autor() != null && dto.autor().toLowerCase().contains(criterio.getAutor().toLowerCase());
                    }
                    if (criterio.tieneCategoria()) {
                        matchCategoria = dto.categoria() != null && dto.categoria().equalsIgnoreCase(criterio.getCategoria());
                    }
                    if (criterio.tieneRangoPrecio()) {
                        matchPrecio = criterio.getRangoPrecio().estaDentroDelRango(dto.precio());
                    }
                    return matchTitulo && matchAutor && matchCategoria && matchPrecio;
                })
                .map(dto -> new com.openlib.market.domain.catalogo.LibroCatalogo(dto.isbn(), dto.titulo(), dto.precio(), dto.urlPortada()))
                .toList();
    }

    @Override
    public List<com.openlib.market.domain.catalogo.LibroCatalogo> buscarRelacionados(
            com.openlib.market.domain.catalogo.CriterioSimilitud criterio) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.estado() == null || dto.estado().equals("ACTIVO"))
                .filter(dto -> !dto.isbn().equals(criterio.getIsbnExcluido()))
                .filter(dto -> dto.categoria() != null && dto.categoria().equalsIgnoreCase(criterio.getCategoria()))
                .limit(criterio.getLimite())
                .map(dto -> new com.openlib.market.domain.catalogo.LibroCatalogo(dto.isbn(), dto.titulo(), dto.precio(), dto.urlPortada()))
                .toList();
    }

    public List<LibroDto> listarTodos() {
        return baseDatosEnMemoria;
    }

    public record LibroDto(String isbn, String titulo, double precio, String urlPortada, String autor, String categoria, String idVendedor, String estado) {
        public LibroDto(String isbn, String titulo, double precio, String urlPortada, String autor, String categoria, String idVendedor) {
            this(isbn, titulo, precio, urlPortada, autor, categoria, idVendedor, "ACTIVO");
        }
        public LibroDto(String isbn, String titulo, double precio, String urlPortada, String autor, String categoria) {
            this(isbn, titulo, precio, urlPortada, autor, categoria, null, "ACTIVO");
        }
    }
}
