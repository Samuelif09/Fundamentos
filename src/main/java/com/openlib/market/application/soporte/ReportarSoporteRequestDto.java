package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.ElementoReportado;

public class ReportarSoporteRequestDto {
    private final String idDenunciante;
    private final ElementoReportado tipoElemento;
    private final String idElemento;
    private final String motivo;

    public ReportarSoporteRequestDto(String idDenunciante, ElementoReportado tipoElemento, String idElemento, String motivo) {
        this.idDenunciante = idDenunciante;
        this.tipoElemento = tipoElemento;
        this.idElemento = idElemento;
        this.motivo = motivo;
    }

    public String getIdDenunciante() { return idDenunciante; }
    public ElementoReportado getTipoElemento() { return tipoElemento; }
    public String getIdElemento() { return idElemento; }
    public String getMotivo() { return motivo; }
}
