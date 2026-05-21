package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Version;

/**
 * @Version habilita concurrencia optimista (Optimistic Locking).
 * Si dos transacciones leen y actualizan el mismo saldo simultáneamente,
 * la segunda lanzará OptimisticLockException, evitando el doble gasto.
 */
@Entity
@Table(name = "billeteras")
public class BilleteraEntity {

    @Id
    private String idVendedor;

    @Column(nullable = false)
    private double saldoDisponible;

    @Version
    private Long version;

    public BilleteraEntity() {}

    public BilleteraEntity(String idVendedor, double saldoDisponible) {
        this.idVendedor = idVendedor;
        this.saldoDisponible = saldoDisponible;
    }

    public String getIdVendedor() { return idVendedor; }
    public double getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(double saldoDisponible) { this.saldoDisponible = saldoDisponible; }
    public Long getVersion() { return version; }
}
