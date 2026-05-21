package com.openlib.market.domain.catalogo;

import java.util.List;

public interface IInventarioGateway {
    List<LibroCatalogo> listarPorVendedorId(String idVendedor);
}
