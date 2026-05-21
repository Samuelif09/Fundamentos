package com.openlib.market.domain.tienda;

import com.openlib.market.domain.catalogo.LibroCatalogo;

import java.util.List;

public class PerfilTienda {
    private final String idVendedor;
    private final String nombreTienda;
    private final UrlAmigable urlAmigable;
    private final List<LibroCatalogo> catalogoPublico;

    public PerfilTienda(String idVendedor, String nombreTienda, UrlAmigable urlAmigable, List<LibroCatalogo> catalogoPublico) {
        this.idVendedor = idVendedor;
        this.nombreTienda = nombreTienda;
        this.urlAmigable = urlAmigable;
        this.catalogoPublico = catalogoPublico;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getNombreTienda() { return nombreTienda; }
    public UrlAmigable getUrlAmigable() { return urlAmigable; }
    public List<LibroCatalogo> getCatalogoPublico() { return catalogoPublico; }
}
