package com.openlib.market.infrastructure.explorar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.explorar.ITendenciaGateway;
import com.openlib.market.domain.explorar.LibroTendencia;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TendenciaJsonGateway implements ITendenciaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroTendenciaDto> baseDatosEnMemoria;

    public TendenciaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("libros_tendencia.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LibroTendenciaDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (this.baseDatosEnMemoria == null || this.baseDatosEnMemoria.isEmpty()) {
            // Datos quemados para la Entrega 1 si no hay JSON
            this.baseDatosEnMemoria = List.of(
                new LibroTendenciaDto("978-1", "Libro A", 500, 4.8, LocalDate.of(2023, 10, 1)),
                new LibroTendenciaDto("978-2", "Libro B", 200, 4.2, LocalDate.of(2023, 11, 15)),
                new LibroTendenciaDto("978-3", "Libro C", 1000, 4.9, LocalDate.of(2022, 5, 20))
            );
        }
    }

    @Override
    public List<LibroTendencia> obtenerTodos() {
        return baseDatosEnMemoria.stream()
                .map(dto -> new LibroTendencia(
                        dto.isbn(),
                        dto.titulo(),
                        dto.ventasTotales(),
                        dto.calificacion(),
                        dto.fechaPublicacion()
                ))
                .collect(Collectors.toList());
    }

    private record LibroTendenciaDto(String isbn, String titulo, int ventasTotales, double calificacion, LocalDate fechaPublicacion) {}
}
