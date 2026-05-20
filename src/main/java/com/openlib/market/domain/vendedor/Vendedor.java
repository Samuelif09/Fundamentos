package com.openlib.market.domain.vendedor;

import java.util.UUID;

public class Vendedor {
    private final String id;
    private final String idUsuario;
    private final RazonSocial razonSocial;
    private final IdentificacionTributaria identificacionTributaria;
    private EstadoVerificacion estadoVerificacion;

    public Vendedor(String idUsuario, RazonSocial razonSocial, IdentificacionTributaria identificacionTributaria) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }
        this.id = UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.razonSocial = razonSocial;
        this.identificacionTributaria = identificacionTributaria;
        this.estadoVerificacion = EstadoVerificacion.NO_INICIADO;
    }

    public Vendedor(String id, String idUsuario, RazonSocial razonSocial, IdentificacionTributaria identificacionTributaria) {
        this(id, idUsuario, razonSocial, identificacionTributaria, EstadoVerificacion.NO_INICIADO);
    }

    public Vendedor(String id, String idUsuario, RazonSocial razonSocial, IdentificacionTributaria identificacionTributaria, EstadoVerificacion estadoVerificacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.razonSocial = razonSocial;
        this.identificacionTributaria = identificacionTributaria;
        this.estadoVerificacion = estadoVerificacion != null ? estadoVerificacion : EstadoVerificacion.NO_INICIADO;
    }

    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public RazonSocial getRazonSocial() { return razonSocial; }
    public IdentificacionTributaria getIdentificacionTributaria() { return identificacionTributaria; }
    public EstadoVerificacion getEstadoVerificacion() { return estadoVerificacion; }

    public void solicitarVerificacion() {
        if (this.estadoVerificacion == EstadoVerificacion.EN_REVISION) {
            throw new VerificacionEnCursoException();
        }
        if (this.estadoVerificacion == EstadoVerificacion.APROBADO) {
            throw new VendedorYaVerificadoException();
        }
        this.estadoVerificacion = EstadoVerificacion.EN_REVISION;
    }

    public void aprobar() {
        if (this.estadoVerificacion != EstadoVerificacion.EN_REVISION) {
            throw new SolicitudInvalidaException("El vendedor no está en revisión.");
        }
        this.estadoVerificacion = EstadoVerificacion.APROBADO;
    }
}

