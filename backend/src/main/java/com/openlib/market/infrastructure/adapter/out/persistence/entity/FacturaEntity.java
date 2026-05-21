package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
public class FacturaEntity {

    @Id
    private String idFactura;
    
    @Column(nullable = false)
    private String idPedido;
    
    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    // Datos Vendedor
    private String vendedorId;
    private String vendedorIdentificacion;
    private String vendedorRazonSocial;

    // Datos Comprador
    private String compradorId;
    private String compradorNombre;
    private String compradorCorreo;

    // Impuestos
    private double impuestoSubtotal;
    private double impuestoIva;
    private double impuestoTotal;

    public FacturaEntity() {}

    public FacturaEntity(String idFactura, String idPedido, LocalDateTime fechaEmision, 
                         String vendedorId, String vendedorIdentificacion, String vendedorRazonSocial, 
                         String compradorId, String compradorNombre, String compradorCorreo, 
                         double impuestoSubtotal, double impuestoIva, double impuestoTotal) {
        this.idFactura = idFactura;
        this.idPedido = idPedido;
        this.fechaEmision = fechaEmision;
        this.vendedorId = vendedorId;
        this.vendedorIdentificacion = vendedorIdentificacion;
        this.vendedorRazonSocial = vendedorRazonSocial;
        this.compradorId = compradorId;
        this.compradorNombre = compradorNombre;
        this.compradorCorreo = compradorCorreo;
        this.impuestoSubtotal = impuestoSubtotal;
        this.impuestoIva = impuestoIva;
        this.impuestoTotal = impuestoTotal;
    }

    public String getIdFactura() { return idFactura; }
    public void setIdFactura(String idFactura) { this.idFactura = idFactura; }

    public String getIdPedido() { return idPedido; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }

    public String getVendedorIdentificacion() { return vendedorIdentificacion; }
    public void setVendedorIdentificacion(String vendedorIdentificacion) { this.vendedorIdentificacion = vendedorIdentificacion; }

    public String getVendedorRazonSocial() { return vendedorRazonSocial; }
    public void setVendedorRazonSocial(String vendedorRazonSocial) { this.vendedorRazonSocial = vendedorRazonSocial; }

    public String getCompradorId() { return compradorId; }
    public void setCompradorId(String compradorId) { this.compradorId = compradorId; }

    public String getCompradorNombre() { return compradorNombre; }
    public void setCompradorNombre(String compradorNombre) { this.compradorNombre = compradorNombre; }

    public String getCompradorCorreo() { return compradorCorreo; }
    public void setCompradorCorreo(String compradorCorreo) { this.compradorCorreo = compradorCorreo; }

    public double getImpuestoSubtotal() { return impuestoSubtotal; }
    public void setImpuestoSubtotal(double impuestoSubtotal) { this.impuestoSubtotal = impuestoSubtotal; }

    public double getImpuestoIva() { return impuestoIva; }
    public void setImpuestoIva(double impuestoIva) { this.impuestoIva = impuestoIva; }

    public double getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(double impuestoTotal) { this.impuestoTotal = impuestoTotal; }
}
