package com.openlib.market.application.notificacion;

import com.openlib.market.domain.notificacion.EmailDestino;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.notificacion.ReciboCompra;
import com.openlib.market.domain.pago.PedidoCompletadoEvent;

public class RecibirPostCompraInteractor {

    private final INotificacionGateway notificacionGateway;

    public RecibirPostCompraInteractor(INotificacionGateway notificacionGateway) {
        this.notificacionGateway = notificacionGateway;
    }

    // Este método será invocado por el adaptador de infraestructura que escucha el bus de eventos
    public void onPedidoCompletado(PedidoCompletadoEvent evento) {
        ReciboCompra recibo = new ReciboCompra(evento.getIdPedido(), evento.getTotalPagado());
        
        // En un caso real, el evento podría contener el email del usuario o deberíamos 
        // usar un IUsuarioGateway para buscar el email asociado a evento.getIdUsuario().
        // Para este requerimiento asumiremos que el ID del usuario provisto en el request 
        // (y en el evento) es temporalmente su email para mantener la simplicidad o buscamos 
        // un dummy.
        
        // Asumiendo que el ID del usuario en el Request de la Entrega 1 es directamente el email
        String emailString = evento.getIdUsuario() != null && evento.getIdUsuario().contains("@") 
                             ? evento.getIdUsuario() 
                             : "comprador@openlib.com";
        
        EmailDestino destino = new EmailDestino(emailString);

        notificacionGateway.enviarReciboEmail(destino, recibo);
    }
}
