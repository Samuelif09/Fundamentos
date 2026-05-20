package com.openlib.market.infrastructure.busqueda;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.busqueda.IBusquedaGateway;
import com.openlib.market.domain.busqueda.LibroBuscado;
import com.openlib.market.domain.busqueda.PalabraClave;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusquedaJsonGateway implements IBusquedaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LibroBuscado> baseDatosEnMemoria;

    public BusquedaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("libros.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LibroBuscado>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = Collections.emptyList();
            }
        } else {
            // Datos quemados si no existe el archivo
            this.baseDatosEnMemoria = List.of(
                new LibroBuscado("1", "Clean Code", "Robert C. Martin"),
                new LibroBuscado("2", "Spring Boot in Action", "Craig Walls"),
                new LibroBuscado("3", "Domain-Driven Design", "Eric Evans"),
                new LibroBuscado("4", "Java 25 Features", "Brian Goetz")
            );
        }
    }

    @Override
    public List<LibroBuscado> buscarPorPalabraClave(PalabraClave palabraClave) {
        String keyword = palabraClave.getValor();
        
        return baseDatosEnMemoria.stream()
                .filter(libro -> 
                    (libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(keyword)) ||
                    (libro.getAutor() != null && libro.getAutor().toLowerCase().contains(keyword))
                )
                .collect(Collectors.toList());
    }
}
