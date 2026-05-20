package com.openlib.market.infrastructure.antifraude;

import com.openlib.market.domain.antifraude.EvaluacionFraude;
import com.openlib.market.domain.antifraude.IAntifraudeGateway;
import com.openlib.market.domain.antifraude.MotivoAlerta;
import com.openlib.market.domain.antifraude.RiesgoTransaccion;
import org.springframework.stereotype.Component;

@Component
public class AntifraudeDummyGateway implements IAntifraudeGateway {

    @Override
    public EvaluacionFraude evaluarTransaccion(String idPedido, double monto) {
        // Lógica Dummy: transacciones mayores a 1000 se consideran alto riesgo.
        if (monto > 1000.0) {
            return new EvaluacionFraude(idPedido, new RiesgoTransaccion(95), MotivoAlerta.MONTO_ANORMAL);
        }
        
        return new EvaluacionFraude(idPedido, new RiesgoTransaccion(10), MotivoAlerta.NINGUNO);
    }
}
