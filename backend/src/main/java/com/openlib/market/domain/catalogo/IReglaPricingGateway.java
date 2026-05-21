package com.openlib.market.domain.catalogo;

import java.util.Optional;
import com.openlib.market.domain.detalle.Isbn;

public interface IReglaPricingGateway {
    void guardar(ReglaPricing regla);
    Optional<ReglaPricing> obtenerRegla(Isbn idLibro, String idVendedor);
}
