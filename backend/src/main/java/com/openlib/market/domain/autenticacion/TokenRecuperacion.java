package com.openlib.market.domain.autenticacion;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenRecuperacion {
    private final String valor;
    private final LocalDateTime fechaExpiracion;

    public TokenRecuperacion(String valor, LocalDateTime fechaExpiracion) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El token no puede ser vacío");
        }
        if (fechaExpiracion == null) {
            throw new IllegalArgumentException("La fecha de expiración es obligatoria");
        }
        this.valor = valor;
        this.fechaExpiracion = fechaExpiracion;
    }

    public static TokenRecuperacion generarNuevo() {
        // Expira en 15 minutos por defecto
        return new TokenRecuperacion(UUID.randomUUID().toString(), LocalDateTime.now().plusMinutes(15));
    }

    public String getValor() { return valor; }
    
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }

    public boolean estaExpirado(LocalDateTime fechaActual) {
        return fechaActual.isAfter(fechaExpiracion);
    }
}
