package com.openlib.market.infrastructure.ventavendedor;

import com.openlib.market.domain.ventavendedor.INotificacionVendedorGateway;
import com.openlib.market.domain.ventavendedor.NotificacionVendedor;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class NotificacionLoggerGateway implements INotificacionVendedorGateway {

    private static final Logger logger = Logger.getLogger(NotificacionLoggerGateway.class.getName());

    @Override
    public void notificarVenta(NotificacionVendedor notificacion) {
        // En una implementación real enviaría un Email o Push Notification
        logger.info(String.format(">>>> NOTIFICACIÓN PUSH al Vendedor [%s]: ¡Enhorabuena! Tienes una nueva venta (Pedido %s). Libros vendidos: %s",
                notificacion.getIdVendedor(),
                notificacion.getIdPedido(),
                notificacion.getIsbnsVendidos()));
    }
}
