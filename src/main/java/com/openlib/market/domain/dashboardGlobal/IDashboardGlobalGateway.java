package com.openlib.market.domain.dashboardGlobal;

import com.openlib.market.domain.pago.Pedido;
import java.util.List;

public interface IDashboardGlobalGateway {
    List<Pedido> obtenerPedidosExitososDePlataforma(int anio);
}
