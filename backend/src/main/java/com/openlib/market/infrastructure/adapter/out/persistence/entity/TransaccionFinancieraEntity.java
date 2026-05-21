package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name = "transacciones_financieras")
public class TransaccionFinancieraEntity {

    @Id
    private String idTransaccion;

    @Column(nullable = false)
    private String idVendedor;

    @Column(nullable = false)
    private double subtotal;

    @Column(nullable = false)
    private LocalDate fecha;

    public TransaccionFinancieraEntity() {}

    public TransaccionFinancieraEntity(String idTransaccion, String idVendedor, double subtotal, LocalDate fecha) {
        this.idTransaccion = idTransaccion;
        this.idVendedor = idVendedor;
        this.subtotal = subtotal;
        this.fecha = fecha;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public String getIdVendedor() { return idVendedor; }
    public double getSubtotal() { return subtotal; }
    public LocalDate getFecha() { return fecha; }
}
