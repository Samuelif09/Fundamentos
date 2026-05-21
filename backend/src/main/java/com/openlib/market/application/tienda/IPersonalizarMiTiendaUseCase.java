package com.openlib.market.application.tienda;

public interface IPersonalizarMiTiendaUseCase {
    void subirBanner(String idVendedor, byte[] contenido, String mimeType, String nombreOriginal);
}
