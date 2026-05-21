package com.openlib.market.infrastructure.inventario;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.inventario.IPromocionGateway;
import com.openlib.market.domain.inventario.PeriodoPromocion;
import com.openlib.market.domain.inventario.PorcentajeDescuento;
import com.openlib.market.domain.inventario.PromocionLibro;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromocionJsonGateway implements IPromocionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<PromocionDto> baseDatosEnMemoria;

    public PromocionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("promociones.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<PromocionDto>>() {});
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
    public void guardar(PromocionLibro promocion) {
        PromocionDto dto = new PromocionDto(
                promocion.getId(),
                promocion.getIsbn(),
                promocion.getDescuento().getValor(),
                promocion.getPeriodo().getFechaInicio(),
                promocion.getPeriodo().getFechaFin()
        );
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public List<PromocionLibro> obtenerPorIsbn(String isbn) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.isbn().equals(isbn))
                .map(dto -> new PromocionLibro(
                        dto.id(),
                        dto.isbn(),
                        new PorcentajeDescuento(dto.descuento()),
                        new PeriodoPromocion(dto.fechaInicio(), dto.fechaFin())
                ))
                .collect(Collectors.toList());
    }

    private record PromocionDto(String id, String isbn, int descuento, LocalDate fechaInicio, LocalDate fechaFin) {}
}
