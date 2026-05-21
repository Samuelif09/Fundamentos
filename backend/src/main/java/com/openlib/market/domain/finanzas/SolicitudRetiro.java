package com.openlib.market.domain.finanzas;

import java.util.UUID;

public class SolicitudRetiro {
    private final String id;
    private final String idVendedor;
    private final MontoRetiro monto;
    private final CuentaDestino cuentaDestino;
    private final EstadoRetiro estado;

    public SolicitudRetiro(String idVendedor, MontoRetiro monto, CuentaDestino cuentaDestino) {
        this(UUID.randomUUID().toString(), idVendedor, monto, cuentaDestino, EstadoRetiro.PENDIENTE);
    }

    public SolicitudRetiro(String id, String idVendedor, MontoRetiro monto, CuentaDestino cuentaDestino, EstadoRetiro estado) {
        this.id = id;
        this.idVendedor = idVendedor;
        this.monto = monto;
        this.cuentaDestino = cuentaDestino;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdVendedor() { return idVendedor; }
    public MontoRetiro getMonto() { return monto; }
    public CuentaDestino getCuentaDestino() { return cuentaDestino; }
    public EstadoRetiro getEstado() { return estado; }
}
