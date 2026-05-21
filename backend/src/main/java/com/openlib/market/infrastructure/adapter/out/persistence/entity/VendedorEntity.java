package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vendedores")
public class VendedorEntity {

    @Id
    private String id;

    @Column(name = "id_usuario", nullable = false)
    private String idUsuario;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "identificacion_tributaria", nullable = false, unique = true)
    private String identificacionTributaria;

    @Column(name = "estado_verificacion", nullable = false)
    private String estadoVerificacion;

    public VendedorEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getIdentificacionTributaria() { return identificacionTributaria; }
    public void setIdentificacionTributaria(String identificacionTributaria) { this.identificacionTributaria = identificacionTributaria; }

    public String getEstadoVerificacion() { return estadoVerificacion; }
    public void setEstadoVerificacion(String estadoVerificacion) { this.estadoVerificacion = estadoVerificacion; }
}
