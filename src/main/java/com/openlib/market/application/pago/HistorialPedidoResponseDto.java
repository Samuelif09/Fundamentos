package com.openlib.market.application.pago;

import java.time.LocalDateTime;

public class HistorialPedidoResponseDto {
    private String id;
    private double total;
    private String estado;
    private LocalDateTime fecha;

    public HistorialPedidoResponseDto(String id, double total, String estado, LocalDateTime fecha) {
        this.id = id;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }
}
