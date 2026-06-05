package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacciones_billetera")
public class TransaccionBilleteraEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String idVendedor;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String tipo; // "SALE", "COMMISSION", "WITHDRAWAL"

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private double monto;

    public TransaccionBilleteraEntity() {}

    public TransaccionBilleteraEntity(String idVendedor, LocalDateTime fecha, String tipo, String descripcion, double monto) {
        this.id = UUID.randomUUID().toString();
        this.idVendedor = idVendedor;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public String getId() { return id; }
    public String getIdVendedor() { return idVendedor; }
    public LocalDateTime getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
}
