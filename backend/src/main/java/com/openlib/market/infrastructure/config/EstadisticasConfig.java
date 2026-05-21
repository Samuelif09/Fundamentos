package com.openlib.market.infrastructure.config;

import com.openlib.market.application.estadisticas.IVerEstadisticasMiCuentaUseCase;
import com.openlib.market.application.estadisticas.VerEstadisticasMiCuentaInteractor;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.pago.IPedidoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstadisticasConfig {

    @Bean
    public IVerEstadisticasMiCuentaUseCase verEstadisticasMiCuentaUseCase(
            IPedidoGateway pedidoGateway,
            IHistorialNavegacionGateway historialGateway,
            IDetalleGateway detalleGateway) {
        return new VerEstadisticasMiCuentaInteractor(pedidoGateway, historialGateway, detalleGateway);
    }
}
