package com.openlib.market.application.comunicado;

public class ComunicadoDto {
    private final String id;
    private final String asunto;
    private final String fechaEnvio;
    private final int cantidadDestinatarios;

    public ComunicadoDto(String id, String asunto, String fechaEnvio, int cantidadDestinatarios) {
        this.id = id;
        this.asunto = asunto;
        this.fechaEnvio = fechaEnvio;
        this.cantidadDestinatarios = cantidadDestinatarios;
    }

    public String getId() { return id; }
    public String getAsunto() { return asunto; }
    public String getFechaEnvio() { return fechaEnvio; }
    public int getCantidadDestinatarios() { return cantidadDestinatarios; }
}
