package com.openlib.market.application.finanzas;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.finanzas.DesgloseFinanciero;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.ReglaComisionDomainService;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import com.openlib.market.domain.finanzas.TransaccionLiquidacion;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VerDesgloseFinanzasInteractor implements IVerDesgloseFinanzasUseCase {

    private final ILiquidacionGateway liquidacionGateway;
    private final ReglaComisionDomainService reglaComisionDomainService;

    public VerDesgloseFinanzasInteractor(ILiquidacionGateway liquidacionGateway, ReglaComisionDomainService reglaComisionDomainService) {
        this.liquidacionGateway = liquidacionGateway;
        this.reglaComisionDomainService = reglaComisionDomainService;
    }

    @Override
    public List<DesgloseFinancieroDto> obtenerDesglose(String idVendedor) {
        List<TransaccionFinanciera> transacciones = liquidacionGateway.obtenerTransaccionesPorVendedor(idVendedor);

        List<TransaccionLiquidacion> liquidaciones = transacciones.stream()
                .map(t -> new TransaccionLiquidacion(t, reglaComisionDomainService.calcularDesglose(t.getSubtotal())))
                .toList();

        return liquidaciones.stream().map(l -> {
            DesgloseFinanciero d = l.getDesglose();
            return new DesgloseFinancieroDto(
                    l.getTransaccionBase().getIdTransaccion(),
                    l.getTransaccionBase().getFecha(),
                    d.getMontoBruto(),
                    d.getComisionPlataforma(),
                    d.getImpuestos(),
                    d.getGananciaNeta()
            );
        }).collect(Collectors.toList());
    }
}
