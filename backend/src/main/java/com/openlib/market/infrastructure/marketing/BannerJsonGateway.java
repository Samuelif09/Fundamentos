package com.openlib.market.infrastructure.marketing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.marketing.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BannerJsonGateway implements IBannerGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<BannerDto> baseDatosEnMemoria;

    public BannerJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("banners.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<BannerDto>>() {});
            } catch (Exception e) {
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
    public void guardar(BannerPromocional banner) {
        baseDatosEnMemoria.add(new BannerDto(
                banner.getId(),
                banner.getTitulo(),
                banner.getUrlImagen(),
                banner.getUrlDestino(),
                banner.getPeriodo().getFechaInicio().toString(),
                banner.getPeriodo().getFechaFin().toString(),
                banner.getEstado().name()
        ));
        guardarDatos();
    }

    @Override
    public void actualizar(BannerPromocional banner) {
        baseDatosEnMemoria.removeIf(b -> b.id().equals(banner.getId()));
        guardar(banner);
    }

    @Override
    public BannerPromocional obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(b -> b.id().equals(id))
                .findFirst()
                .map(this::mapToDomain)
                .orElse(null);
    }

    @Override
    public List<BannerPromocional> listarTodos() {
        return baseDatosEnMemoria.stream()
                .map(this::mapToDomain)
                .toList();
    }

    private BannerPromocional mapToDomain(BannerDto dto) {
        PeriodoCampana periodo = new PeriodoCampana(
                LocalDateTime.parse(dto.fechaInicio()),
                LocalDateTime.parse(dto.fechaFin())
        );
        return new BannerPromocional(
                dto.id(),
                dto.titulo(),
                dto.urlImagen(),
                dto.urlDestino(),
                periodo,
                EstadoCampana.valueOf(dto.estado())
        );
    }

    private record BannerDto(String id, String titulo, String urlImagen, String urlDestino, String fechaInicio, String fechaFin, String estado) {}
}
