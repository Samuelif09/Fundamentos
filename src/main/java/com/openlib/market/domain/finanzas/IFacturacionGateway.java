package com.openlib.market.domain.finanzas;

import java.util.Optional;

public interface IFacturacionGateway {
    void guardarFactura(FacturaTributaria factura);
    Optional<FacturaTributaria> obtenerPorId(String idFactura);
}
