package com.openlib.market.domain.dashboard;

/**
 * Value Object que representa un indicador clave de rendimiento (KPI).
 * Empaqueta nombre, valor numérico actual y variación porcentual vs. el día anterior.
 */
public class Metrica {

    private final String nombre;
    private final double valor;
    private final double variacionPorcentual;

    public Metrica(String nombre, double valor, double variacionPorcentual) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la métrica no puede ser vacío.");
        }
        this.nombre = nombre;
        this.valor = valor;
        this.variacionPorcentual = variacionPorcentual;
    }

    public String getNombre() { return nombre; }
    public double getValor() { return valor; }
    public double getVariacionPorcentual() { return variacionPorcentual; }
}
