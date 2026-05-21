package com.openlib.market.infrastructure.vendedor;

import com.openlib.market.domain.vendedor.INotificacionAdminGateway;
import org.springframework.stereotype.Component;

@Component
public class NotificacionAdminLogGateway implements INotificacionAdminGateway {

    @Override
    public void notificarVerificacionPendiente(String idVendedor) {
        System.out.println("LOG: Se ha enviado una notificación al administrador sobre la verificación pendiente del vendedor " + idVendedor);
    }

    @Override
    public void notificarVendedorAprobado(String idVendedor) {
        System.out.println("LOG: Se ha enviado una notificación al vendedor " + idVendedor + " informando su aprobación.");
    }
}
