package com.openlib.market.infrastructure.notificacion;

import com.openlib.market.domain.notificacion.EmailDestino;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.notificacion.ReciboCompra;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class NotificacionLoggerGateway implements INotificacionGateway {
    private static final Logger logger = Logger.getLogger(NotificacionLoggerGateway.class.getName());

    @Override
    public void enviarReciboEmail(EmailDestino destino, ReciboCompra recibo) {
        String mensaje = String.format("Simulando envío de email a: %s\nAsunto: Confirmación de Compra OpenLib Market\nCuerpo: Su pedido %s ha sido procesado exitosamente por un total de $%.2f",
                destino.getEmail(), recibo.getIdPedido(), recibo.getTotalPagado());
        
        logger.info(mensaje);
        System.out.println("==================================================");
        System.out.println(mensaje);
        System.out.println("==================================================");
    }
}
