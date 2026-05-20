package com.openlib.market.domain.soporte;

import java.util.UUID;

public class Disputa {
    private final String id;
    private final String idPedido;
    private final String idComprador;
    private final String idVendedor;
    private final String motivo;
    
    private EstadoDisputa estado;
    private Resolucion resolucion;

    public Disputa(String idPedido, String idComprador, String idVendedor, String motivo) {
        if (idPedido == null || idPedido.trim().isEmpty()) throw new IllegalArgumentException("El pedido es obligatorio");
        if (idComprador == null || idComprador.trim().isEmpty()) throw new IllegalArgumentException("El comprador es obligatorio");
        if (idVendedor == null || idVendedor.trim().isEmpty()) throw new IllegalArgumentException("El vendedor es obligatorio");
        if (motivo == null || motivo.trim().isEmpty()) throw new IllegalArgumentException("El motivo es obligatorio");

        this.id = UUID.randomUUID().toString();
        this.idPedido = idPedido;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.motivo = motivo;
        this.estado = EstadoDisputa.ABIERTA;
        this.resolucion = Resolucion.PENDIENTE;
    }

    public Disputa(String id, String idPedido, String idComprador, String idVendedor, String motivo, EstadoDisputa estado, Resolucion resolucion) {
        this.id = id;
        this.idPedido = idPedido;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.motivo = motivo;
        this.estado = estado;
        this.resolucion = resolucion;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public String getIdComprador() { return idComprador; }
    public String getIdVendedor() { return idVendedor; }
    public String getMotivo() { return motivo; }
    public EstadoDisputa getEstado() { return estado; }
    public Resolucion getResolucion() { return resolucion; }

    public void iniciarMediacion() {
        if (estado != EstadoDisputa.ABIERTA) {
            throw new TransicionEstadoInvalidaException("Solo se pueden mediar disputas ABIERTAS.");
        }
        this.estado = EstadoDisputa.EN_MEDIACION;
    }

    public ReembolsoSolicitadoPorDisputaEvent resolver(Resolucion dictamen) {
        if (estado != EstadoDisputa.EN_MEDIACION) {
            throw new TransicionEstadoInvalidaException("La disputa debe estar EN_MEDIACION para ser resuelta.");
        }
        if (dictamen == Resolucion.PENDIENTE) {
            throw new IllegalArgumentException("La resolución no puede ser PENDIENTE.");
        }

        this.resolucion = dictamen;
        this.estado = EstadoDisputa.RESUELTA;

        if (dictamen == Resolucion.FAVOR_COMPRADOR) {
            return new ReembolsoSolicitadoPorDisputaEvent(idPedido, id);
        }
        return null;
    }
}
