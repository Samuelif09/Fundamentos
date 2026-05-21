package com.openlib.market.application.carrito;

public class AgregarItemRequestDto {
    private String sesionId; // Para visitantes
    private String idUsuario; // Para compradores
    private String libroIsbn;
    private int cantidad;

    public AgregarItemRequestDto() {}

    public AgregarItemRequestDto(String sesionId, String idUsuario, String libroIsbn, int cantidad) {
        this.sesionId = sesionId;
        this.idUsuario = idUsuario;
        this.libroIsbn = libroIsbn;
        this.cantidad = cantidad;
    }

    public String getSesionId() { return sesionId; }
    public void setSesionId(String sesionId) { this.sesionId = sesionId; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getLibroIsbn() { return libroIsbn; }
    public void setLibroIsbn(String libroIsbn) { this.libroIsbn = libroIsbn; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
