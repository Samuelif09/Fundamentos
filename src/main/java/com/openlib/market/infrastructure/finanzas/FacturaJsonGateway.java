package com.openlib.market.infrastructure.finanzas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openlib.market.domain.finanzas.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FacturaJsonGateway implements IFacturacionGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<FacturaDto> baseDatosEnMemoria;

    public FacturaJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.jsonFile = new File("facturas.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<FacturaDto>>() {});
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
    public void guardarFactura(FacturaTributaria factura) {
        FacturaDto dto = new FacturaDto(
                factura.getIdFactura(),
                factura.getIdPedido(),
                factura.getFechaEmision(),
                new VendedorDto(factura.getVendedor().getIdVendedor(), factura.getVendedor().getIdentificacionTributaria(), factura.getVendedor().getRazonSocial()),
                new CompradorDto(factura.getComprador().getIdUsuario(), factura.getComprador().getNombre(), factura.getComprador().getCorreo()),
                new ImpuestosDto(factura.getDesgloseImpuestos().getSubtotal(), factura.getDesgloseImpuestos().getIva(), factura.getDesgloseImpuestos().getTotal())
        );

        baseDatosEnMemoria.add(dto);
        guardarDatos();
    }

    @Override
    public Optional<FacturaTributaria> obtenerPorId(String idFactura) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.idFactura().equals(idFactura))
                .map(dto -> new FacturaTributaria(
                        dto.idFactura(),
                        dto.idPedido(),
                        dto.fechaEmision(),
                        new DatosFiscalesVendedor(dto.vendedor().idVendedor(), dto.vendedor().identificacionTributaria(), dto.vendedor().razonSocial()),
                        new DatosFiscalesComprador(dto.comprador().idUsuario(), dto.comprador().nombre(), dto.comprador().correo()),
                        new DesgloseImpuestos(dto.impuestos().subtotal()) // Se recalcula en constructor, o podria mapearse directo
                ))
                .findFirst();
    }

    private record FacturaDto(String idFactura, String idPedido, LocalDateTime fechaEmision, VendedorDto vendedor, CompradorDto comprador, ImpuestosDto impuestos) {}
    private record VendedorDto(String idVendedor, String identificacionTributaria, String razonSocial) {}
    private record CompradorDto(String idUsuario, String nombre, String correo) {}
    private record ImpuestosDto(double subtotal, double iva, double total) {}
}
