package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.IntervaloTiempo;
import com.openlib.market.domain.finanzas.PuntoDatos;
import com.openlib.market.domain.finanzas.SerieTiempoVentas;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;

import java.util.List;

public class VerGraficasVentasInteractor implements IVerGraficasVentasUseCase {

    private final ILiquidacionGateway liquidacionGateway;
    private final SerieTiempoVentas serieTiempoVentas;

    public VerGraficasVentasInteractor(ILiquidacionGateway liquidacionGateway) {
        this.liquidacionGateway = liquidacionGateway;
        this.serieTiempoVentas = new SerieTiempoVentas();
    }

    @Override
    public List<PuntoDatos> verGrafica(String idVendedor, IntervaloTiempo intervalo) {
        if (idVendedor == null || idVendedor.isEmpty() || intervalo == null) {
            throw new IllegalArgumentException("Parámetros inválidos");
        }

        List<TransaccionFinanciera> transacciones = liquidacionGateway.obtenerTransaccionesPorVendedor(idVendedor);
        return serieTiempoVentas.agrupar(transacciones, intervalo);
    }
}
