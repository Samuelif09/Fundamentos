package com.openlib.market.infrastructure.finanzas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LiquidacionJsonGateway implements ILiquidacionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<LiquidacionDto> baseDatosEnMemoria;

    public LiquidacionJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("liquidaciones.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<LiquidacionDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
            // Semilla de datos para pruebas
            this.baseDatosEnMemoria.add(new LiquidacionDto("t1", "seller-1", 120.50, LocalDate.now().minusDays(5)));
            this.baseDatosEnMemoria.add(new LiquidacionDto("t2", "seller-1", 80.00, LocalDate.now().minusDays(2)));
            this.baseDatosEnMemoria.add(new LiquidacionDto("t3", "seller-2", 45.00, LocalDate.now().minusDays(1)));
        }
    }

    @Override
    public List<TransaccionFinanciera> obtenerTransaccionesPorVendedor(String idVendedor) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.idVendedor().equals(idVendedor))
                .map(dto -> new TransaccionFinanciera(dto.idTransaccion(), dto.subtotal(), dto.fecha()))
                .collect(Collectors.toList());
    }

    private record LiquidacionDto(String idTransaccion, String idVendedor, double subtotal, LocalDate fecha) {}
}
