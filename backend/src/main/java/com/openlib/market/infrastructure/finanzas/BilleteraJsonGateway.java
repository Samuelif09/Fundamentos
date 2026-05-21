package com.openlib.market.infrastructure.finanzas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.finanzas.BilleteraVendedor;
import com.openlib.market.domain.finanzas.IBilleteraGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BilleteraJsonGateway implements IBilleteraGateway {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile = new File("billeteras.json");
    private List<BilleteraDto> baseDatosEnMemoria;

    public BilleteraJsonGateway() {
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<>() {});
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
    public Optional<BilleteraVendedor> obtenerPorIdVendedor(String idVendedor) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.idVendedor().equals(idVendedor))
                .map(dto -> new BilleteraVendedor(dto.idVendedor(), dto.saldo()))
                .findFirst();
    }

    @Override
    public void guardar(BilleteraVendedor billetera) {
        baseDatosEnMemoria.removeIf(dto -> dto.idVendedor().equals(billetera.getIdVendedor()));
        baseDatosEnMemoria.add(new BilleteraDto(billetera.getIdVendedor(), billetera.getSaldoDisponible()));
        guardarDatos();
    }

    private record BilleteraDto(String idVendedor, double saldo) {}
}
