package com.openlib.market.application.dashboard;

public class WidgetDto {
    private final String tipo;
    private final int posX;
    private final int posY;
    private final int ancho;
    private final int alto;

    public WidgetDto(String tipo, int posX, int posY, int ancho, int alto) {
        this.tipo = tipo;
        this.posX = posX;
        this.posY = posY;
        this.ancho = ancho;
        this.alto = alto;
    }

    public String getTipo() { return tipo; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
}
