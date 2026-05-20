package com.openlib.market.domain.soporte;

import java.util.UUID;

public class ReporteContenido {
    private final String id;
    private final String idDenunciante;
    private final ElementoReportado elementoReportado;
    private final String idElemento;
    private final String motivo;
    private final EstadoReporte estado;

    public ReporteContenido(String idDenunciante, ElementoReportado elementoReportado, String idElemento, String motivo) {
        if (idDenunciante == null || idDenunciante.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del denunciante es obligatorio");
        }
        if (elementoReportado == null) {
            throw new IllegalArgumentException("El tipo de elemento reportado es obligatorio");
        }
        if (idElemento == null || idElemento.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del elemento es obligatorio");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo es obligatorio");
        }

        this.id = UUID.randomUUID().toString();
        this.idDenunciante = idDenunciante;
        this.elementoReportado = elementoReportado;
        this.idElemento = idElemento;
        this.motivo = motivo;
        this.estado = EstadoReporte.PENDIENTE; // Todo reporte nace pendiente
    }

    // Constructor para rehidratar desde BD
    public ReporteContenido(String id, String idDenunciante, ElementoReportado elementoReportado, String idElemento, String motivo, EstadoReporte estado) {
        this.id = id;
        this.idDenunciante = idDenunciante;
        this.elementoReportado = elementoReportado;
        this.idElemento = idElemento;
        this.motivo = motivo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdDenunciante() { return idDenunciante; }
    public ElementoReportado getElementoReportado() { return elementoReportado; }
    public String getIdElemento() { return idElemento; }
    public String getMotivo() { return motivo; }
    public EstadoReporte getEstado() { return estado; }
}
