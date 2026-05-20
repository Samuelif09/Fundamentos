package com.openlib.market.application.notificacion;

import com.openlib.market.domain.listadeseos.IListaDeseosGateway;
import com.openlib.market.domain.notificacionrebaja.INotificacionGateway;
import com.openlib.market.domain.notificacionrebaja.LibroCambioPrecioEvent;

import java.util.List;

public class RecibirNotificacionPrecioInteractor {

    private final IListaDeseosGateway listaDeseosGateway;
    private final INotificacionGateway notificacionGateway;

    public RecibirNotificacionPrecioInteractor(IListaDeseosGateway listaDeseosGateway, INotificacionGateway notificacionGateway) {
        this.listaDeseosGateway = listaDeseosGateway;
        this.notificacionGateway = notificacionGateway;
    }

    public void manejarCambioPrecio(LibroCambioPrecioEvent evento) {
        if (evento.getPrecioNuevo() < evento.getPrecioAnterior()) {
            List<String> usuarios = listaDeseosGateway.obtenerUsuariosInteresados(evento.getIdLibro());
            for (String idUsuario : usuarios) {
                notificacionGateway.enviarAlertaPrecio(idUsuario, evento.getIdLibro(), evento.getPrecioNuevo());
            }
        }
    }
}
