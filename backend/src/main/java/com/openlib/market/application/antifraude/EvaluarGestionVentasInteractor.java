package com.openlib.market.application.antifraude;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.antifraude.EvaluacionFraude;
import com.openlib.market.domain.antifraude.IAntifraudeGateway;

@Service
public class EvaluarGestionVentasInteractor implements IEvaluarGestionVentasUseCase {

    private final IAntifraudeGateway antifraudeGateway;

    public EvaluarGestionVentasInteractor(IAntifraudeGateway antifraudeGateway) {
        this.antifraudeGateway = antifraudeGateway;
    }

    @Override
    public boolean evaluarTransaccion(String idPedido, double monto) {
        EvaluacionFraude evaluacion = antifraudeGateway.evaluarTransaccion(idPedido, monto);
        
        if (evaluacion.requiereBloqueo()) {
            // En una implementación real, aquí se emitiría un evento para bloquear el pedido
            // o se llamaría al IPedidoGateway para marcarlo como FRAUDULENTO.
            System.err.println("ALERTA ANTIFRAUDE: Pedido " + idPedido + " bloqueado por riesgo alto.");
            return false; // Transacción bloqueada
        }
        
        return true; // Transacción aprobada
    }
}
