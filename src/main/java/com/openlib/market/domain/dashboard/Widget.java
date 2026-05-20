package com.openlib.market.domain.dashboard;

public class Widget {
    private final TipoWidget tipo;
    private Posicion posicion;
    private Tamano tamano;

    public Widget(TipoWidget tipo, Posicion posicion, Tamano tamano) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de widget es obligatorio");
        if (posicion == null) throw new IllegalArgumentException("La posición es obligatoria");
        if (tamano == null) throw new IllegalArgumentException("El tamaño es obligatorio");

        this.tipo = tipo;
        this.posicion = posicion;
        this.tamano = tamano;
    }

    public TipoWidget getTipo() { return tipo; }
    public Posicion getPosicion() { return posicion; }
    public Tamano getTamano() { return tamano; }
}
