package com.openlib.market.domain.finanzas;

import java.util.List;

public interface ILiquidacionGateway {
    List<TransaccionFinanciera> obtenerTransaccionesPorVendedor(String idVendedor);
}
