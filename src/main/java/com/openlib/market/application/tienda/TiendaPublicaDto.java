package com.openlib.market.application.tienda;

import com.openlib.market.domain.catalogo.LibroCatalogo;
import java.util.List;

public class TiendaPublicaDto {
    private final String idVendedor;
    private final String nombreTienda;
    private final String urlAmigable;
    private final List<LibroCatalogo> libros;

    public TiendaPublicaDto(String idVendedor, String nombreTienda, String urlAmigable, List<LibroCatalogo> libros) {
        this.idVendedor = idVendedor;
        this.nombreTienda = nombreTienda;
        this.urlAmigable = urlAmigable;
        this.libros = libros;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getNombreTienda() { return nombreTienda; }
    public String getUrlAmigable() { return urlAmigable; }
    public List<LibroCatalogo> getLibros() { return libros; }
}
