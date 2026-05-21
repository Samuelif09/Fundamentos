package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class WidgetEmbeddable {
    private String tipo;
    private int posicionX;
    private int posicionY;
    private int tamanoAncho;
    private int tamanoAlto;

    public WidgetEmbeddable() {}

    public WidgetEmbeddable(String tipo, int posicionX, int posicionY, int tamanoAncho, int tamanoAlto) {
        this.tipo = tipo;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.tamanoAncho = tamanoAncho;
        this.tamanoAlto = tamanoAlto;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getPosicionX() { return posicionX; }
    public void setPosicionX(int posicionX) { this.posicionX = posicionX; }

    public int getPosicionY() { return posicionY; }
    public void setPosicionY(int posicionY) { this.posicionY = posicionY; }

    public int getTamanoAncho() { return tamanoAncho; }
    public void setTamanoAncho(int tamanoAncho) { this.tamanoAncho = tamanoAncho; }

    public int getTamanoAlto() { return tamanoAlto; }
    public void setTamanoAlto(int tamanoAlto) { this.tamanoAlto = tamanoAlto; }
}
