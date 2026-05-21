package com.openlib.market.infrastructure.catalogo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.detalle.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ContenidoDigitalJsonGateway implements IContenidoDigitalGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ContenidoDigitalDto> baseDatosEnMemoria;

    public ContenidoDigitalJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("contenidos.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ContenidoDigitalDto>>() {});
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
    public void guardarContenido(ContenidoDigital contenido) {
        Integer duracion = null;
        if (contenido instanceof Audiolibro) {
            duracion = ((Audiolibro) contenido).getDuracion().getValor();
        } else if (contenido instanceof CursoVirtual) {
            duracion = ((CursoVirtual) contenido).getDuracionEstimada().getValor();
        }

        ContenidoDigitalDto dto = new ContenidoDigitalDto(
                contenido.getId().getValor(),
                contenido.getTitulo(),
                contenido.getSinopsis(),
                contenido.getPrecio().getValor(),
                contenido.getUrlPortada(),
                contenido.getCategoria(),
                contenido.getIdVendedor(),
                contenido.getTipoFormato().name(),
                contenido.getEstado().name(),
                contenido.getUrlVistaPrevia(),
                duracion
        );
        
        baseDatosEnMemoria.removeIf(c -> c.id().equals(dto.id()));
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public Optional<ContenidoDigital> obtenerContenidoPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .map(dto -> DigitalContentFactory.crear(
                        TipoFormato.valueOf(dto.tipoFormato()),
                        new Isbn(dto.id()),
                        dto.titulo(),
                        dto.sinopsis(),
                        new Precio(dto.precio()),
                        dto.urlPortada(),
                        dto.categoria(),
                        dto.idVendedor(),
                        dto.duracionMinutos()
                ))
                .findFirst();
    }

    public record ContenidoDigitalDto(
            String id,
            String titulo,
            String sinopsis,
            double precio,
            String urlPortada,
            String categoria,
            String idVendedor,
            String tipoFormato,
            String estado,
            String urlVistaPrevia,
            Integer duracionMinutos
    ) {}
}
