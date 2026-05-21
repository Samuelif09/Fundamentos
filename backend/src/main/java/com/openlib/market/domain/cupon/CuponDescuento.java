package com.openlib.market.domain.cupon;

import java.time.LocalDate;

public class CuponDescuento {
    private final CodigoCupon codigo;
    private final EstrategiaDescuento estrategia;
    private final LocalDate fechaExpiracion;

    public CuponDescuento(CodigoCupon codigo, EstrategiaDescuento estrategia, LocalDate fechaExpiracion) {
        if (codigo == null) throw new IllegalArgumentException("El código es obligatorio");
        if (estrategia == null) throw new IllegalArgumentException("La estrategia de descuento es obligatoria");
        if (fechaExpiracion == null) throw new IllegalArgumentException("La fecha de expiración es obligatoria");
        this.codigo = codigo;
        this.estrategia = estrategia;
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * Valida si el cupón es aplicable. Lanza excepción si está expirado.
     */
    public void validar(LocalDate hoy) {
        if (hoy.isAfter(fechaExpiracion)) {
            throw new CuponExpiradoException(codigo.getValor());
        }
    }

    public double aplicarDescuento(double total) {
        return estrategia.aplicar(total);
    }

    public CodigoCupon getCodigo() { return codigo; }
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
}
