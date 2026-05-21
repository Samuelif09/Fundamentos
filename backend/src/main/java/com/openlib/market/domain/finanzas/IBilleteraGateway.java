package com.openlib.market.domain.finanzas;

import java.util.Optional;

public interface IBilleteraGateway {
    Optional<BilleteraVendedor> obtenerPorIdVendedor(String idVendedor);
    void guardar(BilleteraVendedor billetera);
}
