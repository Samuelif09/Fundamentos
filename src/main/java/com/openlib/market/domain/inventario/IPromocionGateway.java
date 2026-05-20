package com.openlib.market.domain.inventario;

import java.util.List;

public interface IPromocionGateway {
    void guardar(PromocionLibro promocion);
    List<PromocionLibro> obtenerPorIsbn(String isbn);
}
