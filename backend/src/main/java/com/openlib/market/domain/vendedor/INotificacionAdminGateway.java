package com.openlib.market.domain.vendedor;

public interface INotificacionAdminGateway {
    void notificarVerificacionPendiente(String idVendedor);
    void notificarVendedorAprobado(String idVendedor);
}
