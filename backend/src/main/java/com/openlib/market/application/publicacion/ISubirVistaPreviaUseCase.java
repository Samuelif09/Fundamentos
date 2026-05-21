package com.openlib.market.application.publicacion;

public interface ISubirVistaPreviaUseCase {
    void subirVistaPrevia(String idVendedor, String isbn, byte[] contenidoArchivo, String tipoMime);
}
