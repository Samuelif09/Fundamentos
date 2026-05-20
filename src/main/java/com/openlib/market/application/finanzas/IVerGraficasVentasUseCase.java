package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.IntervaloTiempo;
import com.openlib.market.domain.finanzas.PuntoDatos;

import java.util.List;

public interface IVerGraficasVentasUseCase {
    List<PuntoDatos> verGrafica(String idVendedor, IntervaloTiempo intervalo);
}
