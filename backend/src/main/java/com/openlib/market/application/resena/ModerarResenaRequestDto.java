package com.openlib.market.application.resena;

import jakarta.validation.constraints.NotNull;

public class ModerarResenaRequestDto {

    @NotNull
    private String estado;
    private String motivo;

    public ModerarResenaRequestDto() {}

    public ModerarResenaRequestDto(String estado, String motivo) {
        this.estado = estado;
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
