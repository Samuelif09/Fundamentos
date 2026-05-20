package com.openlib.market.domain.finanzas;

import java.time.LocalDate;

public class Periodo {
    private final LocalDate inicio;
    private final LocalDate fin;

    public Periodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        this.inicio = inicio;
        this.fin = fin;
    }

    public LocalDate getInicio() { return inicio; }
    public LocalDate getFin() { return fin; }

    public boolean contiene(LocalDate fecha) {
        if (fecha == null) return false;
        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }
}
