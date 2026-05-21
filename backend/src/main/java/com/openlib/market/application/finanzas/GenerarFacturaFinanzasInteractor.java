package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.DatosFiscalesComprador;
import com.openlib.market.domain.finanzas.DatosFiscalesVendedor;
import com.openlib.market.domain.finanzas.DesgloseImpuestos;
import com.openlib.market.domain.finanzas.FacturaTributaria;
import com.openlib.market.domain.finanzas.IFacturacionGateway;

public class GenerarFacturaFinanzasInteractor implements IGenerarFacturaFinanzasUseCase {

    private final IFacturacionGateway facturacionGateway;

    public GenerarFacturaFinanzasInteractor(IFacturacionGateway facturacionGateway) {
        this.facturacionGateway = facturacionGateway;
    }

    @Override
    public void generarFactura(GenerarFacturaRequestDto request) {
        FacturaTributaria factura = new FacturaTributaria(
                request.idPedido(),
                new DatosFiscalesVendedor(request.idVendedor(), request.identificacionTributariaVendedor(), request.razonSocialVendedor()),
                new DatosFiscalesComprador(request.idComprador(), request.nombreComprador(), request.correoComprador()),
                new DesgloseImpuestos(request.subtotalPedido())
        );

        facturacionGateway.guardarFactura(factura);
    }
}
