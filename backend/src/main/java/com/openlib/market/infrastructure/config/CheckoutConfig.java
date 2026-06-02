package com.openlib.market.infrastructure.config;

import com.openlib.market.application.notificacion.RecibirPostCompraInteractor;
import com.openlib.market.application.pago.IIngresarCheckoutUseCase;
import com.openlib.market.application.pago.IngresarCheckoutInteractor;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.pago.IEventPublisher;
import com.openlib.market.domain.pago.IPasarelaPagoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckoutConfig {

    @Bean
    public IIngresarCheckoutUseCase ingresarCheckoutUseCase(
            IPasarelaPagoGateway pasarelaPago, 
            IEventPublisher eventPublisher,
            com.openlib.market.domain.carrito.ICarritoGateway carritoGateway,
            com.openlib.market.domain.pago.IPedidoGateway pedidoGateway) {
        return new IngresarCheckoutInteractor(pasarelaPago, eventPublisher, carritoGateway, pedidoGateway);
    }

    @Bean
    public RecibirPostCompraInteractor recibirPostCompraInteractor(
            INotificacionGateway notificacionGateway) {
        return new RecibirPostCompraInteractor(notificacionGateway);
    }

    @Bean
    public com.openlib.market.application.pago.IRealizarPagoUseCase realizarPagoUseCase(
            com.openlib.market.domain.pago.IPedidoGateway pedidoGateway,
            com.openlib.market.domain.pago.IPasarelaPagoFactory pasarelaFactory,
            com.openlib.market.domain.carrito.ICarritoGateway carritoGateway) {
        return new com.openlib.market.application.pago.RealizarPagoInteractor(pedidoGateway, pasarelaFactory, carritoGateway);
    }
}
