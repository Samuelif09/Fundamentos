package com.openlib.market.application.detalle;

public interface IVerDetalleLibroUseCase {
    LibroDetalleCompradorDto verDetalle(String isbn);
    LibroDetalleCompradorDto verDetalle(String isbn, String idUsuario);
}
