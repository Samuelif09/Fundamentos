package com.openlib.market.application.vendedor;

public interface IVerificarRegistroVendedorUseCase {
    void solicitarVerificacion(String idVendedor, byte[] documentoContenido, String mimeType, String nombreOriginal);
}
