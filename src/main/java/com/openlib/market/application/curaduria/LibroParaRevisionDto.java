package com.openlib.market.application.curaduria;

public class LibroParaRevisionDto {
    private final String isbn;
    private final String titulo;
    private final String sinopsis;
    private final double precio;
    private final String urlPortada;
    private final String idVendedor;
    private final String nombreVendedor;
    private final String identificacionTributariaVendedor;

    public LibroParaRevisionDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String idVendedor, String nombreVendedor, String identificacionTributariaVendedor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.urlPortada = urlPortada;
        this.idVendedor = idVendedor;
        this.nombreVendedor = nombreVendedor;
        this.identificacionTributariaVendedor = identificacionTributariaVendedor;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public double getPrecio() { return precio; }
    public String getUrlPortada() { return urlPortada; }
    public String getIdVendedor() { return idVendedor; }
    public String getNombreVendedor() { return nombreVendedor; }
    public String getIdentificacionTributariaVendedor() { return identificacionTributariaVendedor; }
}
