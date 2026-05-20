package com.openlib.market.domain.tienda;

import java.util.Optional;

public interface ITiendaVendedorGateway {
    Optional<PerfilTiendaBase> obtenerPerfilPorSlug(String slug);
    Optional<PerfilTiendaBase> obtenerPerfilPorIdVendedor(String idVendedor);
    void actualizarBanner(String idVendedor, String urlBanner);
    
    // Objeto auxiliar solo para datos del vendedor, los libros vienen de inventario
    record PerfilTiendaBase(String idVendedor, String nombreTienda, String slug, String urlBanner) {}
}
