package com.openlib.market.application.finanzas;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.finanzas.*;

@Service
public class SolicitarRetiroFinanzasInteractor implements ISolicitarRetiroFinanzasUseCase {

    private final IBilleteraGateway billeteraGateway;
    private final IRetiroGateway retiroGateway;

    public SolicitarRetiroFinanzasInteractor(IBilleteraGateway billeteraGateway, IRetiroGateway retiroGateway) {
        this.billeteraGateway = billeteraGateway;
        this.retiroGateway = retiroGateway;
    }

    @Override
    public void solicitarRetiro(String idVendedor, double monto, String cuentaDestinoStr) {
        MontoRetiro montoRetiro = new MontoRetiro(monto);
        CuentaDestino cuentaDestino = new CuentaDestino(cuentaDestinoStr);

        // Obtener billetera (si no existe, saldo 0)
        BilleteraVendedor billetera = billeteraGateway.obtenerPorIdVendedor(idVendedor)
                .orElseGet(() -> new BilleteraVendedor(idVendedor, 0.0));

        // El dominio valida fondos suficientes aquí (lanza FondosInsuficientesException si falla)
        billetera.retirar(montoRetiro);

        // Crear solicitud en estado PENDIENTE
        SolicitudRetiro solicitud = new SolicitudRetiro(idVendedor, montoRetiro, cuentaDestino);

        // Persistir cambios (secuencial, aceptable en MVP JSON)
        billeteraGateway.guardar(billetera);
        retiroGateway.guardar(solicitud);
    }
}
