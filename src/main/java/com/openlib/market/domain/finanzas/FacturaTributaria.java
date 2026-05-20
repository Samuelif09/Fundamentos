package com.openlib.market.domain.finanzas;

import java.time.LocalDateTime;
import java.util.UUID;

public class FacturaTributaria {
    private final String idFactura;
    private final String idPedido;
    private final LocalDateTime fechaEmision;
    private final DatosFiscalesVendedor vendedor;
    private final DatosFiscalesComprador comprador;
    private final DesgloseImpuestos desgloseImpuestos;

    public FacturaTributaria(String idPedido, DatosFiscalesVendedor vendedor, DatosFiscalesComprador comprador, DesgloseImpuestos desgloseImpuestos) {
        if (idPedido == null || idPedido.isBlank()) throw new IllegalArgumentException("ID del pedido requerido");
        if (vendedor == null) throw new IllegalArgumentException("Datos del vendedor requeridos");
        if (comprador == null) throw new IllegalArgumentException("Datos del comprador requeridos");
        if (desgloseImpuestos == null) throw new IllegalArgumentException("Desglose de impuestos requerido");

        this.idFactura = UUID.randomUUID().toString();
        this.idPedido = idPedido;
        this.fechaEmision = LocalDateTime.now();
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.desgloseImpuestos = desgloseImpuestos;
    }

    public FacturaTributaria(String idFactura, String idPedido, LocalDateTime fechaEmision, DatosFiscalesVendedor vendedor, DatosFiscalesComprador comprador, DesgloseImpuestos desgloseImpuestos) {
        this.idFactura = idFactura;
        this.idPedido = idPedido;
        this.fechaEmision = fechaEmision;
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.desgloseImpuestos = desgloseImpuestos;
    }

    public String getIdFactura() { return idFactura; }
    public String getIdPedido() { return idPedido; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public DatosFiscalesVendedor getVendedor() { return vendedor; }
    public DatosFiscalesComprador getComprador() { return comprador; }
    public DesgloseImpuestos getDesgloseImpuestos() { return desgloseImpuestos; }
}
