package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.FacturaTributaria;
import com.openlib.market.domain.finanzas.IFacturacionGateway;
import java.util.Optional;

public class DescargarFacturaUseCase {

    private final IFacturacionGateway facturacionGateway;

    public DescargarFacturaUseCase(IFacturacionGateway facturacionGateway) {
        this.facturacionGateway = facturacionGateway;
    }

    public String descargar(String idFactura) {
        Optional<FacturaTributaria> facturaOpt = facturacionGateway.obtenerPorId(idFactura);
        if (facturaOpt.isEmpty()) {
            throw new IllegalArgumentException("Factura no encontrada");
        }

        FacturaTributaria f = facturaOpt.get();
        // Serialización simulada para MVP
        return String.format("FACTURA ELECTRÓNICA\nID: %s\nFecha: %s\nVendedor: %s (NIT: %s)\nComprador: %s\nSubtotal: $%.2f\nIVA(19%%): $%.2f\nTOTAL: $%.2f",
                f.getIdFactura(), f.getFechaEmision().toString(),
                f.getVendedor().getRazonSocial(), f.getVendedor().getIdentificacionTributaria(),
                f.getComprador().getNombre(),
                f.getDesgloseImpuestos().getSubtotal(),
                f.getDesgloseImpuestos().getIva(),
                f.getDesgloseImpuestos().getTotal());
    }
}
