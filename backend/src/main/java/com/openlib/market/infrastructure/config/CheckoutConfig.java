package com.openlib.market.infrastructure.config;

import com.openlib.market.application.notificacion.RecibirPostCompraInteractor;
import com.openlib.market.application.pago.IIngresarCheckoutUseCase;
import com.openlib.market.application.pago.IngresarCheckoutInteractor;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.pago.IEventPublisher;
import com.openlib.market.domain.pago.IPasarelaPagoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.checkout.CarritoCheckoutObserver;
import com.openlib.market.application.checkout.InventarioCheckoutObserver;
import com.openlib.market.application.checkout.ProcesarCheckoutInteractor;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.checkout.ICheckoutEventPublisher;
import com.openlib.market.domain.checkout.IPasarelaPagoSimuladaGateway;
import com.openlib.market.domain.checkout.PedidoFactory;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;
@Configuration
public class CheckoutConfig {

    @Bean
    public IIngresarCheckoutUseCase ingresarCheckoutUseCase(
            IPasarelaPagoGateway pasarelaPago, 
            IEventPublisher eventPublisher) {
        return new IngresarCheckoutInteractor(pasarelaPago, eventPublisher);
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

    @Bean
    public PedidoFactory pedidoFactory() {
        return new PedidoFactory();
    }

    @Bean
    public ProcesarCheckoutInteractor procesarCheckoutInteractor(
            ICarritoGateway carritoGateway,
            com.openlib.market.domain.pago.IPedidoGateway pedidoGateway,
            IPasarelaPagoSimuladaGateway pasarelaPagoGateway,
            ICheckoutEventPublisher eventPublisher,
            PedidoFactory pedidoFactory,
            IInventarioGateway inventarioGateway,
            IContenidoDigitalGateway contenidoGateway) {
        return new ProcesarCheckoutInteractor(carritoGateway, pedidoGateway, pasarelaPagoGateway, eventPublisher, pedidoFactory, inventarioGateway, contenidoGateway);
    }

    @Bean
    public CarritoCheckoutObserver carritoCheckoutObserver(ICarritoGateway carritoGateway) {
        return new CarritoCheckoutObserver(carritoGateway);
    }

    @Bean
    public InventarioCheckoutObserver inventarioCheckoutObserver(
            com.openlib.market.domain.pago.IPedidoGateway pedidoGateway,
            IInventarioGateway inventarioGateway,
            IContenidoDigitalGateway contenidoGateway) {
        return new InventarioCheckoutObserver(pedidoGateway, inventarioGateway, contenidoGateway);
    }
}
