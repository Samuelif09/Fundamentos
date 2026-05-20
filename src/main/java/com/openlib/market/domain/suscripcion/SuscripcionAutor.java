package com.openlib.market.domain.suscripcion;

import java.time.LocalDateTime;

public class SuscripcionAutor {
    private final String idComprador;
    private final String idVendedor;
    private final LocalDateTime fechaSuscripcion;

    public SuscripcionAutor(String idComprador, String idVendedor) {
        if (idComprador == null || idComprador.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del comprador es obligatorio");
        }
        if (idVendedor == null || idVendedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del vendedor es obligatorio");
        }
        if (idComprador.equals(idVendedor)) {
            throw new OperacionInvalidaException("Un usuario no puede seguirse a sí mismo.");
        }

        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.fechaSuscripcion = LocalDateTime.now();
    }

    public SuscripcionAutor(String idComprador, String idVendedor, LocalDateTime fechaSuscripcion) {
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public String getIdComprador() { return idComprador; }
    public String getIdVendedor() { return idVendedor; }
    public LocalDateTime getFechaSuscripcion() { return fechaSuscripcion; }
}
