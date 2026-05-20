package com.openlib.market.domain.pago;

public class TransaccionPago {
    public enum Estado { PENDIENTE_AUTORIZACION, APROBADA, RECHAZADA }

    private final String idTransaccion;
    private final TokenPago token;
    private final Monto monto;
    private Estado estado;

    public TransaccionPago(String idTransaccion, TokenPago token, Monto monto) {
        this.idTransaccion = idTransaccion;
        this.token = token;
        this.monto = monto;
        this.estado = Estado.PENDIENTE_AUTORIZACION;
    }

    public void aprobar() { this.estado = Estado.APROBADA; }
    public void rechazar() { this.estado = Estado.RECHAZADA; }

    public String getIdTransaccion() { return idTransaccion; }
    public TokenPago getToken() { return token; }
    public Monto getMonto() { return monto; }
    public Estado getEstado() { return estado; }
}
