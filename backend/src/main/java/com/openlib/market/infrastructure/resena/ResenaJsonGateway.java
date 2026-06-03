package com.openlib.market.infrastructure.resena;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.resena.Calificacion;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ResenaJsonGateway implements IResenaGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ResenaDto> baseDatosEnMemoria;

    public ResenaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("resenas.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ResenaDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
        
        if (this.baseDatosEnMemoria.isEmpty()) {
            this.baseDatosEnMemoria.add(new ResenaDto("1", "978-3-16-148410-0", 5, "Un libro espectacular, cambió mi forma de programar.", LocalDate.now().minusDays(2), null));
            this.baseDatosEnMemoria.add(new ResenaDto("2", "978-3-16-148410-0", 4, "Muy bueno, pero un poco denso.", LocalDate.now().minusDays(5), null));
            this.baseDatosEnMemoria.add(new ResenaDto("3", "978-3-16-148410-0", 5, "Lectura obligatoria para todos.", LocalDate.now().minusDays(10), null));
        }
    }

    @Override
    public List<Resena> buscarResenasPorIsbn(String isbn) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.isbnLibro().equals(isbn))
                .map(dto -> new Resena(dto.id(), dto.isbnLibro(), new Calificacion(dto.calificacion()), dto.texto(), dto.fecha()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Resena> listarPorLibroId(String isbnLibro, int offset, int limit) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.isbnLibro().equals(isbnLibro))
                .skip(offset)
                .limit(limit)
                .map(dto -> new Resena(dto.id(), dto.isbnLibro(), new Calificacion(dto.calificacion()), dto.texto(), dto.fecha()))
                .toList();
    }

    @Override
    public Optional<Resena> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .map(dto -> {
                    Resena r = new Resena(dto.id(), dto.isbnLibro(), new Calificacion(dto.calificacion()), dto.texto(), dto.fecha());
                    if (dto.respuestaVendedor() != null) {
                        r.responder(new com.openlib.market.domain.resena.ComentarioRespuesta(dto.respuestaVendedor()));
                    }
                    return r;
                })
                .findFirst();
    }

    @Override
    public void actualizar(Resena resena) {
        String respuesta = resena.getRespuestaVendedor() != null ? resena.getRespuestaVendedor().getTexto() : null;
        baseDatosEnMemoria.removeIf(dto -> dto.id().equals(resena.getId()));
        baseDatosEnMemoria.add(new ResenaDto(resena.getId(), resena.getIsbnLibro(),
                resena.getCalificacion().getValor(), resena.getTexto(), resena.getFecha(), respuesta));
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(String id) {
        baseDatosEnMemoria.removeIf(dto -> dto.id().equals(id));
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private record ResenaDto(String id, String isbnLibro, int calificacion, String texto, LocalDate fecha, String respuestaVendedor) {}
}
