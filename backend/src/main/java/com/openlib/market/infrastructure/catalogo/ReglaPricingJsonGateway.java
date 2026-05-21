package com.openlib.market.infrastructure.catalogo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.catalogo.*;
import com.openlib.market.domain.detalle.Isbn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReglaPricingJsonGateway implements IReglaPricingGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<ReglaPricingDto> baseDatosEnMemoria;

    public ReglaPricingJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("reglas_pricing.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<ReglaPricingDto>>() {});
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
    public void guardar(ReglaPricing regla) {
        ReglaPricingDto dto = new ReglaPricingDto(
                regla.getIdLibro().getValor(),
                regla.getIdVendedor(),
                regla.getPrecioMinimo().getValor(),
                regla.getPrecioMaximo().getValor(),
                regla.getEstrategia().name()
        );

        baseDatosEnMemoria.removeIf(r -> r.idLibro().equals(dto.idLibro()) && r.idVendedor().equals(dto.idVendedor()));
        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public Optional<ReglaPricing> obtenerRegla(Isbn idLibro, String idVendedor) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.idLibro().equals(idLibro.getValor()) && dto.idVendedor().equals(idVendedor))
                .map(dto -> new ReglaPricing(
                        new Isbn(dto.idLibro()),
                        dto.idVendedor(),
                        new PrecioMinimo(dto.precioMinimo()),
                        new PrecioMaximo(dto.precioMaximo()),
                        EstrategiaCompetencia.valueOf(dto.estrategia())
                ))
                .findFirst();
    }

    private record ReglaPricingDto(String idLibro, String idVendedor, double precioMinimo, double precioMaximo, String estrategia) {}
}
