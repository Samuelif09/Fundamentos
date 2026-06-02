package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.*;

public class SolicitarRetiroFinanzasInteractor implements ISolicitarRetiroFinanzasUseCase {

    private final IBilleteraGateway billeteraGateway;
    private final IRetiroGateway retiroGateway;
    private final com.openlib.market.infrastructure.adapter.out.persistence.repository.TransaccionBilleteraRepository transaccionBilleteraRepository;

    public SolicitarRetiroFinanzasInteractor(IBilleteraGateway billeteraGateway, IRetiroGateway retiroGateway,
                                             com.openlib.market.infrastructure.adapter.out.persistence.repository.TransaccionBilleteraRepository transaccionBilleteraRepository) {
        this.billeteraGateway = billeteraGateway;
        this.retiroGateway = retiroGateway;
        this.transaccionBilleteraRepository = transaccionBilleteraRepository;
    }

    @Override
    public void solicitarRetiro(String idVendedor, double monto, String cuentaDestinoStr) {
        MontoRetiro montoRetiro = new MontoRetiro(monto);
        CuentaDestino cuentaDestino = new CuentaDestino(cuentaDestinoStr);

        // Obtener billetera (si no existe, saldo 0)
        BilleteraVendedor billetera = billeteraGateway.obtenerPorIdVendedor(idVendedor)
                .orElseGet(() -> new BilleteraVendedor(idVendedor, 0.0));

        // El dominio valida fondos suficientes aqu (lanza FondosInsuficientesException si falla)
        billetera.retirar(montoRetiro);

        // Crear solicitud en estado PENDIENTE
        SolicitudRetiro solicitud = new SolicitudRetiro(idVendedor, montoRetiro, cuentaDestino);

        // Crear registro contable del retiro
        com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionBilleteraEntity tx = 
            new com.openlib.market.infrastructure.adapter.out.persistence.entity.TransaccionBilleteraEntity(
                idVendedor, java.time.LocalDateTime.now(), "WITHDRAWAL", "Retiro a cuenta: " + cuentaDestinoStr, -monto
            );

        // Persistir cambios
        transaccionBilleteraRepository.save(tx);
        billeteraGateway.guardar(billetera);
        retiroGateway.guardar(solicitud);
    }
}
