package com.openlib.market.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDateTime;

public interface VentaVendedorProjection {
    LocalDateTime getFecha();
    int getCantidad();
    double getPrecioUnitario();
    String getPedidoId();
}
