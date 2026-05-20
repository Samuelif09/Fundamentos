package com.openlib.market.domain.inventario;

import java.time.LocalDate;

public class PeriodoPromocion {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public PeriodoPromocion(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas del periodo promocional no pueden ser nulas");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio de la promoción no puede estar en el pasado");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    public boolean seSolapaCon(PeriodoPromocion otro) {
        return (this.fechaInicio.isBefore(otro.fechaFin) || this.fechaInicio.isEqual(otro.fechaFin)) &&
               (otro.fechaInicio.isBefore(this.fechaFin) || otro.fechaInicio.isEqual(this.fechaFin));
    }

    public boolean estaActivo(LocalDate fecha) {
        return (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
               (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin));
    }
}
