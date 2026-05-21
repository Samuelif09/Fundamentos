package com.openlib.market.application.inventario;

import java.util.List;

public interface IVerInventarioUseCase {
    List<LibroInventarioDto> listarPorVendedor(String idVendedor);
}
