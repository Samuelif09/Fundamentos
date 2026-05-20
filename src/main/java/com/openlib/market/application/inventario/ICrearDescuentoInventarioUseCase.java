package com.openlib.market.application.inventario;

import java.time.LocalDate;

public interface ICrearDescuentoInventarioUseCase {
    void crearDescuento(String idVendedor, String isbn, int porcentaje, LocalDate fechaInicio, LocalDate fechaFin);
}
